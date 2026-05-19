package com.comercializadora.modelo.servicios;

import com.comercializadora.modelo.entidades.Cliente;
import com.comercializadora.modelo.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio repositorio;

    public Cliente guardar(Cliente cliente) {
        return repositorio.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscar(Long id) {
        return repositorio.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return repositorio.findByNombreClienteContainingIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarPorTipo(String tipo) {
        return repositorio.findByTipoClienteIgnoreCase(tipo);
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
