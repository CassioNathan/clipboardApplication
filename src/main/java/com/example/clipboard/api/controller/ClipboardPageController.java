package com.example.clipboard.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        System.out.println(ipServidor);
        model.addAttribute("port", "4002");
        model.addAttribute("serverIp", ipServidor);
        return "clipboard"; // nome da view
    }
}
