package com.comercializadora.controladores;

import com.comercializadora.modelo.entidades.Producto;
import com.comercializadora.modelo.servicios.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductoControlador {

    @Autowired
    private ProductoServicio servicio;

    @GetMapping("/productos")
    public String listar(@RequestParam(required = false) String buscar, Model modelo) {
        if (buscar != null && !buscar.isBlank()) {
            modelo.addAttribute("productos", servicio.buscarPorNombre(buscar));
            modelo.addAttribute("buscar", buscar);
        } else {
            modelo.addAttribute("productos", servicio.listar());
        }
        return "producto";
    }

    @GetMapping("/registro/producto")
    public String registrar(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        return "formularioproducto";
    }

    @PostMapping("/guardar/producto")
    public String guardar(@ModelAttribute Producto producto) {
        servicio.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/producto/{id}")
    public String editar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("producto", servicio.buscar(id).orElse(new Producto()));
        return "formularioproducto";
    }

    @GetMapping("/eliminar/producto/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/productos";
    }
}
