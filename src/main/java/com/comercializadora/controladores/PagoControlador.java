package com.comercializadora.controladores;

import com.comercializadora.modelo.entidades.Pago;
import com.comercializadora.modelo.servicios.ClienteServicio;
import com.comercializadora.modelo.servicios.PagoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class PagoControlador {

    @Autowired private PagoServicio servicio;
    @Autowired private ClienteServicio clienteServicio;

    @GetMapping("/pagos")
    public String listar(@RequestParam(required = false) String estado, Model modelo) {
        if (estado != null && !estado.isBlank()) {
            modelo.addAttribute("pagos", servicio.buscarPorEstado(estado));
            modelo.addAttribute("filtroEstado", estado);
        } else {
            modelo.addAttribute("pagos", servicio.listar());
        }
        modelo.addAttribute("totalCompletados", servicio.totalCompletados());
        return "pago";
    }

    @GetMapping("/registro/pago")
    public String registrar(Model modelo) {
        modelo.addAttribute("pago", new Pago());
        modelo.addAttribute("clientes", clienteServicio.listar());
        return "formulariopago";
    }

    @PostMapping("/guardar/pago")
    public String guardar(
            @RequestParam(required = false) Long idPago,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String tipoPago,
            @RequestParam(required = false) Double montoPago,
            @RequestParam(required = false) String fechaPago,
            @RequestParam(required = false) String estadoPago,
            @RequestParam(required = false) String referencia) {

        Pago pago = (idPago != null)
                ? servicio.buscar(idPago).orElse(new Pago())
                : new Pago();

        pago.setTipoPago(tipoPago);
        pago.setMontoPago(montoPago);
        pago.setEstadoPago(estadoPago);
        pago.setReferencia(referencia);
        if (fechaPago != null && !fechaPago.isBlank()) {
            pago.setFechaPago(LocalDate.parse(fechaPago));
        }
        if (clienteId != null) {
            clienteServicio.buscar(clienteId).ifPresent(pago::setCliente);
        }
        servicio.guardar(pago);
        return "redirect:/pagos";
    }

    @GetMapping("/editar/pago/{id}")
    public String editar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("pago", servicio.buscar(id).orElse(new Pago()));
        modelo.addAttribute("clientes", clienteServicio.listar());
        return "formulariopago";
    }

    @GetMapping("/eliminar/pago/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/pagos";
    }
}
