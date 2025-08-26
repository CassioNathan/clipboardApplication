package com.example.clipboard;

import com.example.clipboard.api.controller.ClipboardController;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.awt.*;

@SpringBootApplication
@EnableScheduling
public class ClipboardApplication {

    @Autowired
    private ClipboardController clipboardController; // instância gerenciada pelo Spring

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");

        SpringApplication.run(ClipboardApplication.class, args);

        WebViewApp.launchViewer();
        ClipboardHotkey.start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onAppReady() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray não é suportado.");
            return;
        }

        PopupMenu popup = new PopupMenu();

        Image image = Toolkit.getDefaultToolkit().createImage(
                ClipboardApplication.class.getResource("/icon.png")
        );

        TrayIcon trayIcon = new TrayIcon(image, "Clipboard API", popup);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Erro ao adicionar ícone na bandeja.");
            e.printStackTrace();
        }

        MenuItem openBrowserItem = new MenuItem("Abrir no Navegador");
        MenuItem openWebViewItem = new MenuItem("Abrir no Webview");
        MenuItem limparListaItem = new MenuItem("Limpar lista");
        MenuItem exitItem = new MenuItem("Sair");

        popup.add(openBrowserItem);
        popup.addSeparator();
        popup.add(openWebViewItem);
        popup.addSeparator();
        popup.add(limparListaItem);
        popup.addSeparator();
        popup.add(exitItem);

        openBrowserItem.addActionListener(e -> {
            try {
                String url = "http://localhost:4002/clipboardArea";
                Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        openWebViewItem.addActionListener(e -> {
            WebViewApp.launchViewer();
        });

        limparListaItem.addActionListener(e -> {
            clipboardController.limparLista();
        });

        exitItem.addActionListener(e -> {
            System.exit(0);
        });
    }
}