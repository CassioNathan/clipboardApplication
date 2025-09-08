package com.example.clipboard.api.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/clipboard")
@CrossOrigin(origins = {
        "vscode-webview://clip",
        "*"
})
public class ClipboardController {

    private final LinkedList<String> clipboardList = new LinkedList<>();
    private final LinkedList<ClipboardFile> clipboardFiles = new LinkedList<>();
    private final Map<String, String> clipboardShare = Collections.synchronizedMap(new LinkedHashMap<>());

    @GetMapping
    public List<String> getClipboardList() {
        return clipboardList;
    }

    @PostMapping("/share")
    public String shareClipboard(@RequestBody Integer itemPosition) {
        String uuid = UUID.randomUUID().toString();

        if (clipboardShare.size() >= 5) {
            String uuidMaisAntigo = clipboardShare.keySet().iterator().next();
            clipboardShare.remove(uuidMaisAntigo);
        }

        clipboardShare.put(uuid, clipboardList.get(itemPosition));
        return uuid;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<String> consumirItem(@PathVariable String uuid) {
        String conteudo = clipboardShare.remove(uuid); // remove ao ler
        if (conteudo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conteudo);
    }

    @PostMapping
    public void addToClipboardList(@RequestBody String texto) {
        clipboardList.addFirst(texto);
        setClipboard(texto);
        if (clipboardList.size() > 5) {
            clipboardList.removeLast();
        }
    }

    @PostMapping("/upload")
    public void uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {

            if (file.getSize() > 100 * 1024 * 1024) { // limite 100 MB
                throw new RuntimeException("Arquivo maior que 100 MB: " + file.getOriginalFilename());
            }

            ClipboardFile cf = new ClipboardFile(file.getOriginalFilename(), file.getBytes());
            clipboardFiles.addFirst(cf);

            if (clipboardFiles.size() > 5) {
                clipboardFiles.removeLast();
            }

            clipboardList.addFirst("Arquivo: " + file.getOriginalFilename());
            if (clipboardList.size() > 5) {
                clipboardList.removeLast();
            }
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        for (ClipboardFile cf : clipboardFiles) {
            if (cf.getFileName().equals(fileName)) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + cf.getFileName() + "\"")
                        .body(cf.getContent());
            }
        }
        throw new RuntimeException("Arquivo não encontrado: " + fileName);
    }

    @GetMapping("/qrcode")
    public ResponseEntity<byte[]> gerarQRCode() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String url = "http://" + inetAddress.getHostAddress() + ":4002/clipboardArea";

        System.out.println(url);

        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(baos.toByteArray());
    }




    public boolean validadeAddToClipboardList(String texto) {
        if (clipboardList.contains(texto)) {
            clipboardList.remove(texto);
            clipboardList.addFirst(texto);
            return false;
        } else {
            return true;
        }
    }

    public void limparLista() {
        clipboardList.clear();
        clipboardFiles.clear();
    }

    // Classe interna para armazenar arquivos
    public static class ClipboardFile {
        private final String fileName;
        private final byte[] content;

        public ClipboardFile(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }
    }

    public void setClipboard(String texto) {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            if (texto.startsWith("data:image")) {
                // É imagem em base64
                String base64 = texto.substring(texto.indexOf(",") + 1);
                byte[] imageBytes = Base64.getDecoder().decode(base64);
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

                Transferable transferable = new Transferable() {
                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[]{DataFlavor.imageFlavor};
                    }

                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return DataFlavor.imageFlavor.equals(flavor);
                    }

                    @Override
                    public Object getTransferData(DataFlavor flavor) {
                        return bufferedImage;
                    }
                };

                clipboard.setContents(transferable, null);
                System.out.println("Imagem colocada no clipboard!");
            } else {
                // Texto normal
                StringSelection selection = new StringSelection(texto);
                clipboard.setContents(selection, null);
                System.out.println("Texto colocado no clipboard!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
