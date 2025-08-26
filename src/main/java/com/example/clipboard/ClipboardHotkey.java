package com.example.clipboard;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClipboardHotkey implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        //SHIFT + F12
        if (e.getKeyCode() == NativeKeyEvent.VC_F12 &&
                (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0) {
            try {
                Desktop.getDesktop().browse(new URI("http://localhost:4002/clipboardArea"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void start() {
        try {
            // Desabilitar logs da lib
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);

            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new ClipboardHotkey());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
