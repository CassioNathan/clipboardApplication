package com.example.clipboard.api.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/clipboard")
@CrossOrigin(origins = {
        "vscode-webview://clip",
        "*"
})
public class ClipboardController {

    private final LinkedList<String> clipboardList = new LinkedList<>();

    // Lista para armazenar arquivos reais
    private final LinkedList<ClipboardFile> clipboardFiles = new LinkedList<>();

    @GetMapping
    public List<String> getClipboardList() {
        return clipboardList;
    }

    @PostMapping
    public void addToClipboardList(@RequestBody String texto) {
        clipboardList.addFirst(texto);
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

            // cria objeto ClipboardFile com nome + conteúdo
            ClipboardFile cf = new ClipboardFile(file.getOriginalFilename(), file.getBytes());
            clipboardFiles.addFirst(cf);

            // mantém apenas os últimos 5 arquivos
            if (clipboardFiles.size() > 5) {
                clipboardFiles.removeLast();
            }

            // opcional: também adiciona o nome na lista de texto
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
}
