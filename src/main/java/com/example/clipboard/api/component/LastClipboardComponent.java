package com.example.clipboard.api.component;

import org.springframework.stereotype.Component;

@Component
public class LastClipboardComponent {
    private String clipboard = "text";

    public String getClipboard() {
        return clipboard;
    }

    public void setClipboard(String clipboard) {
        this.clipboard = clipboard;
    }
}