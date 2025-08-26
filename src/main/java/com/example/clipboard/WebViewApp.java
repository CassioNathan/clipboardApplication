package com.example.clipboard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class WebViewApp extends Application {

    private static boolean launched = false;
    private static WebViewApp instance;

    @Override
    public void start(Stage stage) {
        instance = this; // guarda a instância
        Platform.setImplicitExit(false); // mantém JavaFX vivo
        criarJanela(stage);
    }

    private void criarJanela(Stage stage) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.load("http://localhost:4002/clipboardArea");

        // JS Bridge: permite JS chamar métodos Java
        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("app", new JavaBridge());

        // captura alert do JS
        engine.setOnAlert(event -> System.out.println("Alert do JS: " + event.getData()));

        CheckBox alwaysOnTopCheck = new CheckBox("Always On Top");
        alwaysOnTopCheck.setOnAction(e -> stage.setAlwaysOnTop(alwaysOnTopCheck.isSelected()));
        alwaysOnTopCheck.setTextFill(Color.WHITE);

        HBox topBar = new HBox(alwaysOnTopCheck);
        topBar.setStyle("-fx-background-color: #121212; -fx-padding: 5;");

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(webView);

        Scene scene = new Scene(root, 250, 570);
        stage.setScene(scene);
        stage.setMinWidth(250);
        stage.setMinHeight(500);
        stage.setTitle("Clipboard WebView");
        stage.show();
    }

    public static void launchViewer() {
        if (!launched) {
            launched = true;
            new Thread(() -> Application.launch(WebViewApp.class)).start();
        } else {
            Platform.runLater(() -> {
                Stage novaJanela = new Stage();
                instance.criarJanela(novaJanela);
            });
        }
    }

    // Classe para o JS chamar
    public static class JavaBridge {
        public void baixarArquivo(String url, String nome) {
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, Paths.get(nome), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Arquivo salvo: " + nome);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void mostrarAlerta(String msg) {
            System.out.println("Alerta do JS: " + msg);
        }
    }
}
