package com.example.clipboard.api.service;

import com.example.clipboard.api.component.LastClipboardComponent;
import com.example.clipboard.api.controller.ClipboardController;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

@Service
public class ClipboardService {
    private final LastClipboardComponent lastClipboardComponent;
    private final ClipboardController clipboardController;

    public ClipboardService(LastClipboardComponent lastClipboardComponent, ClipboardController clipboardController) {
        this.lastClipboardComponent = lastClipboardComponent;
        this.clipboardController = clipboardController;
    }

    @Scheduled(fixedDelay = 1000)
    private void getClip() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            // 1. Texto
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String texto = (String) clipboard.getData(DataFlavor.stringFlavor);
                if (clipboardController.validadeAddToClipboardList(texto)) {
                    lastClipboardComponent.setClipboard(texto);
                    clipboardController.addToClipboardList(texto);
                    System.out.println("Texto atualizado: " + texto);
                }
                return;
            }

            // 2. Imagem
            if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                Image image = (Image) clipboard.getData(DataFlavor.imageFlavor);

                int width = image.getWidth(null);
                int height = image.getHeight(null);

                if (width > 0 && height > 0) {
                    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = bufferedImage.createGraphics();
                    g2d.drawImage(image, 0, 0, null);
                    g2d.dispose();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", baos);
                    String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

                    String base64Image = "data:image/png;base64," + base64;

                    if (clipboardController.validadeAddToClipboardList(base64Image)) {
                        lastClipboardComponent.setClipboard(base64Image);
                        clipboardController.addToClipboardList(base64Image);
                        System.out.println("Imagem atualizada no clipboard.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}