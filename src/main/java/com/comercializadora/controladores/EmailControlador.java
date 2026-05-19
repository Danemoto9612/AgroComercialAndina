package com.comercializadora.controladores;

import com.comercializadora.modelo.servicios.EmailServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@Controller
public class EmailControlador {

    @Autowired
    private EmailServicio emailServicio;

    @GetMapping("/email")
    public String form() {
        return "emailForm";
    }

    @PostMapping("/send")
    public String send(@RequestParam String destino,
                       @RequestParam String asunto,
                       @RequestParam(required = false) String nombreUsuario,
                       @RequestParam(required = false) String mensaje,
                       @RequestParam(required = false) MultipartFile archivo,
                       Model modelo) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("nombre",  nombreUsuario != null && !nombreUsuario.isBlank() ? nombreUsuario : "Estimado/a cliente");
            vars.put("mensaje", mensaje != null && !mensaje.isBlank() ? mensaje : "Le informamos sobre una novedad de AgroComercial Andina.");
            emailServicio.enviarConPlantillaYAdjunto(destino, asunto, vars, archivo);
            modelo.addAttribute("resultado", "✅ Correo enviado correctamente a " + destino);
            modelo.addAttribute("tipoAlerta", "alert-success");
        } catch (Exception e) {
            modelo.addAttribute("resultado", "❌ Error al enviar: " + e.getMessage());
            modelo.addAttribute("tipoAlerta", "alert-error");
        }
        return "emailForm";
    }
}
