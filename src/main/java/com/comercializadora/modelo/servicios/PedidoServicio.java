package com.comercializadora.modelo.servicios;

import com.comercializadora.modelo.entidades.Pedido;
import com.comercializadora.modelo.repositorios.PedidoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoServicio {

    @Autowired
    private PedidoRepositorio repositorio;

    public Pedido guardar(Pedido pedido) {
        return repositorio.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listar() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscar(Long id) {
        return repositorio.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorEstado(String estado) {
        return repositorio.findByEstadoPedidoIgnoreCase(estado);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long idCliente) {
        return repositorio.findByClienteIdCliente(idCliente);
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
