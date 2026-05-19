package com.comercializadora.controladores;

import com.comercializadora.modelo.entidades.Cliente;
import com.comercializadora.modelo.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClienteControlador {

    @Autowired
    private ClienteServicio servicio;

    @GetMapping("/clientes")
    public String listar(@RequestParam(required = false) String buscar, Model modelo) {
        if (buscar != null && !buscar.isBlank()) {
            modelo.addAttribute("clientes", servicio.buscarPorNombre(buscar));
            modelo.addAttribute("buscar", buscar);
        } else {
            modelo.addAttribute("clientes", servicio.listar());
        }
        return "cliente";
    }

    @GetMapping("/registro")
    public String registrar(Model modelo) {
        modelo.addAttribute("cliente", new Cliente());
        return "formulariocliente";
    }

    @PostMapping("/guardar/cliente")
    public String guardar(@ModelAttribute Cliente cliente) {
        servicio.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/cliente/{id}")
    public String editar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("cliente", servicio.buscar(id).orElse(new Cliente()));
        return "formulariocliente";
    }

    @GetMapping("/eliminar/cliente/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/clientes";
    }
}
