package com.example.clipboard.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class ClipboardPageController {

    @GetMapping("/clipboardArea")
    public String exibirPaginaClipboard(Model model) {
        String ipServidor = "IP não encontrado";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipServidor = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace(); // ou logue direito
        }

        model.addAttribute("port", "4002");
        model.addAttribute("serverIp", ipServidor);
        return "clipboard";
    }

    @GetMapping("/clipboardShare/{uuid}")
    public String exibirPaginaClipboardShare(@PathVariable String uuid, Model model) {
        String ipServidor = "IP não encontrado";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipServidor = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        model.addAttribute("port", "4002");
        model.addAttribute("serverIp", ipServidor);
        model.addAttribute("ItemUuid", uuid);
        return "shareClipboard";
    }
}
