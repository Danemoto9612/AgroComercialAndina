package com.comercializadora.modelo.servicios;

import com.comercializadora.modelo.entidades.Pago;
import com.comercializadora.modelo.repositorios.PagoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoServicio {

    @Autowired
    private PagoRepositorio repositorio;

    public Pago guardar(Pago pago) {
        return repositorio.save(pago);
    }

    @Transactional(readOnly = true)
    public List<Pago> listar() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pago> buscar(Long id) {
        return repositorio.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pago> buscarPorEstado(String estado) {
        return repositorio.findByEstadoPagoIgnoreCase(estado);
    }

    @Transactional(readOnly = true)
    public List<Pago> buscarPorFechas(LocalDate inicio, LocalDate fin) {
        return repositorio.findByFechaPagoBetween(inicio, fin);
    }

    @Transactional(readOnly = true)
    public Double totalCompletados() {
        Double v = repositorio.sumaTotalPagosCompletados();
        return v != null ? v : 0.0;
    }

    @Transactional(readOnly = true)
    public long contarTotal() {
        return repositorio.count();
    }

    public boolean eliminar(Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return true;
        }
        return false;
    }
}
