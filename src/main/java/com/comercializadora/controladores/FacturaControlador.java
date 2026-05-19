package com.comercializadora.controladores;

import com.comercializadora.modelo.entidades.DetalleFactura;
import com.comercializadora.modelo.entidades.Factura;
import com.comercializadora.modelo.servicios.FacturaServicio;
import com.comercializadora.utilidades.PdfGenerador;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FacturaControlador {

    @Autowired
    private FacturaServicio facturaServicio;

    @GetMapping("/facturas")
    public String lista(@RequestParam(required = false) String buscar, Model modelo) {
        if (buscar != null && !buscar.isBlank()) {
            modelo.addAttribute("facturas", facturaServicio.buscarPorCliente(buscar));
            modelo.addAttribute("buscar", buscar);
        } else {
            modelo.addAttribute("facturas", facturaServicio.buscarTodos());
        }
        modelo.addAttribute("totalFacturado", facturaServicio.totalFacturado());
        return "factura";
    }

    @GetMapping("/registrar")
    public String formulario(Model modelo) {
        modelo.addAttribute("factura", new Factura());
        return "formulariofactura";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Factura factura,
                       @RequestParam(required = false) List<String> producto,
                       @RequestParam(required = false) List<Integer> cantidad,
                       @RequestParam(required = false) List<Double> precio,
                       Model modelo) {
        if (factura.getFecha() == null) factura.setFecha(LocalDate.now());

        List<DetalleFactura> items = new ArrayList<>();
        if (producto != null) {
            for (int i = 0; i < producto.size(); i++) {
                if (producto.get(i) != null && !producto.get(i).isBlank()) {
                    DetalleFactura it = new DetalleFactura();
                    it.setProducto(producto.get(i));
                    it.setCantidad(cantidad.get(i));
                    it.setPrecio(precio.get(i));
                    it.setSubtotal(precio.get(i) * cantidad.get(i));
                    items.add(it);
                }
            }
        }
        factura.setItems(items);
        Factura saved = facturaServicio.saveFactura(factura);
        modelo.addAttribute("resultado", "✅ Factura guardada con código: " + saved.getCodigo() + " (ID: " + saved.getId() + ")");
        modelo.addAttribute("factura", new Factura());
        return "formulariofactura";
    }

    @GetMapping("/pdf/{id}")
    public void pdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Factura factura = facturaServicio.buscarPorId(id);
        if (factura == null) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Factura_" + factura.getCodigo() + ".pdf");
        OutputStream out = response.getOutputStream();
        PdfGenerador.generarFacturaPdf(factura, out);
        out.flush();
    }

    @GetMapping("/eliminar/factura/{id}")
    public String eliminar(@PathVariable Long id) {
        facturaServicio.eliminar(id);
        return "redirect:/facturas";
    }
}
