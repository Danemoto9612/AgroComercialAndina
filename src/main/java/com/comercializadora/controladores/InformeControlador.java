package com.comercializadora.controladores;

import com.comercializadora.modelo.entidades.*;
import com.comercializadora.modelo.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class InformeControlador {

    @Autowired private FacturaServicio facturaServicio;
    @Autowired private PagoServicio pagoServicio;
    @Autowired private PedidoServicio pedidoServicio;
    @Autowired private ProductoServicio productoServicio;
    @Autowired private ClienteServicio clienteServicio;

    // DTO simple para las barras: label, valor, porcentaje (ya calculado en Java)
    public static class BarItem {
        public String label;
        public long valor;
        public long porcentaje;
        public BarItem(String label, long valor, long max) {
            this.label = label;
            this.valor = valor;
            this.porcentaje = max > 0 ? (valor * 100 / max) : 0;
        }
    }

    @GetMapping("/informes")
    @Transactional(readOnly = true)
    public String informes(Model modelo) {

        // ── KPIs ──────────────────────────────────────────────
        modelo.addAttribute("totalFacturado",  facturaServicio.totalFacturado());
        modelo.addAttribute("totalPagado",     pagoServicio.totalCompletados());
        modelo.addAttribute("totalFacturas",   facturaServicio.contarTotal());
        modelo.addAttribute("totalPedidos",    pedidoServicio.contarTotal());
        modelo.addAttribute("totalClientes",   clienteServicio.contarTotal());
        modelo.addAttribute("totalProductos",  productoServicio.contarTotal());

        // ── Pedidos por estado ─────────────────────────────────
        List<Pedido> pedidos = pedidoServicio.listar();
        Map<String, Long> pedMap = pedidos.stream().collect(Collectors.groupingBy(
            p -> p.getEstadoPedido() != null ? p.getEstadoPedido() : "Sin estado",
            Collectors.counting()
        ));
        long maxPed = pedMap.values().stream().mapToLong(v -> v).max().orElse(1L);
        List<BarItem> pedidosBars = pedMap.entrySet().stream()
            .map(e -> new BarItem(e.getKey(), e.getValue(), maxPed))
            .collect(Collectors.toList());
        modelo.addAttribute("pedidosBars", pedidosBars);

        // ── Pagos por tipo ─────────────────────────────────────
        List<Pago> pagos = pagoServicio.listar();
        Map<String, Long> pagoMap = pagos.stream().collect(Collectors.groupingBy(
            p -> p.getTipoPago() != null ? p.getTipoPago() : "Sin tipo",
            Collectors.counting()
        ));
        long maxPago = pagoMap.values().stream().mapToLong(v -> v).max().orElse(1L);
        List<BarItem> pagosBars = pagoMap.entrySet().stream()
            .map(e -> new BarItem(e.getKey(), e.getValue(), maxPago))
            .collect(Collectors.toList());
        modelo.addAttribute("pagosBars", pagosBars);

        // ── Top 5 clientes por pedidos ─────────────────────────
        Map<String, Long> clienteMap = pedidos.stream()
            .filter(p -> p.getCliente() != null)
            .collect(Collectors.groupingBy(
                p -> p.getCliente().getNombreCliente(), Collectors.counting()
            ));
        List<Map.Entry<String, Long>> top5 = clienteMap.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5).collect(Collectors.toList());
        long maxCliente = top5.stream().mapToLong(Map.Entry::getValue).max().orElse(1L);
        List<BarItem> clientesBars = top5.stream()
            .map(e -> new BarItem(e.getKey(), e.getValue(), maxCliente))
            .collect(Collectors.toList());
        modelo.addAttribute("clientesBars", clientesBars);

        // ── Stock bajo ─────────────────────────────────────────
        List<Producto> stockBajo = productoServicio.listar().stream()
            .filter(p -> p.getStockProducto() != null && p.getStockProducto() < 10)
            .collect(Collectors.toList());
        modelo.addAttribute("stockBajo", stockBajo);

        // ── Últimas 5 facturas ─────────────────────────────────
        List<Factura> ultFacturas = facturaServicio.buscarTodos().stream()
            .sorted(Comparator.comparing(Factura::getId).reversed())
            .limit(5).collect(Collectors.toList());
        modelo.addAttribute("ultFacturas", ultFacturas);

        return "informes";
    }
}
