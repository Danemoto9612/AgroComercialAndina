package com.comercializadora.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginControlador {

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model modelo) {

        if (error != null) {
            modelo.addAttribute("error", "Usuario o contraseña incorrectos. Intente de nuevo.");
        }
        if (logout != null) {
            modelo.addAttribute("mensaje", "Ha cerrado sesión correctamente.");
        }
        return "login";
    }
}
