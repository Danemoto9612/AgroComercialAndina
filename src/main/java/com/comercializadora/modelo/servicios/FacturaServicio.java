package com.comercializadora.modelo.servicios;

import com.comercializadora.modelo.entidades.DetalleFactura;
import com.comercializadora.modelo.entidades.Factura;
import com.comercializadora.modelo.repositorios.FacturaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaServicio {

    private static final double IVA_TASA = 0.19;

    @Autowired
    private FacturaRepositorio facturaRepo;

    public Factura saveFactura(Factura factura) {
        double subtotal = 0.0;
        for (DetalleFactura item : factura.getItems()) {
            double sub = item.getPrecio() * item.getCantidad();
            item.setSubtotal(sub);
            item.setFactura(factura);
            subtotal += sub;
        }
        double iva = subtotal * IVA_TASA;
        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(subtotal + iva);
        if (factura.getFecha() == null) {
            factura.setFecha(LocalDate.now());
        }
        return facturaRepo.save(factura);
    }

    public List<Factura> buscarTodos() {
        return facturaRepo.findAll();
    }

    public Factura buscarPorId(Long id) {
        return facturaRepo.findById(id).orElse(null);
    }

    public List<Factura> buscarPorCliente(String cliente) {
        return facturaRepo.findByClienteContainingIgnoreCase(cliente);
    }

    public Double totalFacturado() {
        Double v = facturaRepo.sumaTotalFacturas();
        return v != null ? v : 0.0;
    }

    public long contarTotal() {
        return facturaRepo.count();
    }

    public boolean eliminar(Long id) {
        if (facturaRepo.existsById(id)) {
            facturaRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
