package com.comercializadora.controladores;

import com.comercializadora.modelo.entidades.Pedido;
import com.comercializadora.modelo.servicios.ClienteServicio;
import com.comercializadora.modelo.servicios.PedidoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class PedidoControlador {

    @Autowired private PedidoServicio servicio;
    @Autowired private ClienteServicio clienteServicio;

    @GetMapping("/pedidos")
    public String listar(@RequestParam(required = false) String estado, Model modelo) {
        if (estado != null && !estado.isBlank()) {
            modelo.addAttribute("pedidos", servicio.buscarPorEstado(estado));
            modelo.addAttribute("filtroEstado", estado);
        } else {
            modelo.addAttribute("pedidos", servicio.listar());
        }
        return "pedido";
    }

    @GetMapping("/registro/pedido")
    public String registrar(Model modelo) {
        modelo.addAttribute("pedido", new Pedido());
        modelo.addAttribute("clientes", clienteServicio.listar());
        return "formulariopedido";
    }

    @PostMapping("/guardar/pedido")
    public String guardar(
            @RequestParam(required = false) Long idPedido,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String estadoPedido,
            @RequestParam(required = false) String fechaPedido,
            @RequestParam(required = false) String observaciones) {

        Pedido pedido = (idPedido != null)
                ? servicio.buscar(idPedido).orElse(new Pedido())
                : new Pedido();

        pedido.setEstadoPedido(estadoPedido);
        pedido.setObservaciones(observaciones);
        if (fechaPedido != null && !fechaPedido.isBlank()) {
            pedido.setFechaPedido(LocalDate.parse(fechaPedido));
        }
        if (clienteId != null) {
            clienteServicio.buscar(clienteId).ifPresent(pedido::setCliente);
        }
        servicio.guardar(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/editar/pedido/{id}")
    public String editar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("pedido", servicio.buscar(id).orElse(new Pedido()));
        modelo.addAttribute("clientes", clienteServicio.listar());
        return "formulariopedido";
    }

    @GetMapping("/eliminar/pedido/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/pedidos";
    }
}
