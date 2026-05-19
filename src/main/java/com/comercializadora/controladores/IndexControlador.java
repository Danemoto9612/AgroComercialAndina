package com.comercializadora.controladores;

import com.comercializadora.modelo.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControlador {

    @Autowired private ClienteServicio clienteServicio;
    @Autowired private ProductoServicio productoServicio;
    @Autowired private PedidoServicio pedidoServicio;
    @Autowired private PagoServicio pagoServicio;
    @Autowired private FacturaServicio facturaServicio;

    @GetMapping("/")
    public String principal(Model modelo) {
        modelo.addAttribute("totalClientes",  clienteServicio.contarTotal());
        modelo.addAttribute("totalProductos", productoServicio.contarTotal());
        modelo.addAttribute("totalPedidos",   pedidoServicio.contarTotal());
        modelo.addAttribute("totalPagos",     pagoServicio.contarTotal());
        modelo.addAttribute("totalFacturas",  facturaServicio.contarTotal());
        modelo.addAttribute("montoFacturado", facturaServicio.totalFacturado());
        modelo.addAttribute("montoPagado",    pagoServicio.totalCompletados());
        return "index";
    }
}
