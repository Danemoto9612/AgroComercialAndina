package com.comercializadora.modelo.servicios;

import com.comercializadora.modelo.entidades.Producto;
import com.comercializadora.modelo.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio repositorio;

    public Producto guardar(Producto producto) {
        return repositorio.save(producto);
    }

    public List<Producto> listar() {
        return repositorio.findAll();
    }

    public Optional<Producto> buscar(Long id) {
        return repositorio.findById(id);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return repositorio.findByNombreProductoContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorTipo(String tipo) {
        return repositorio.findByTipoProductoIgnoreCase(tipo);
    }

    public List<Producto> listarDisponibles() {
        return repositorio.findDisponibles();
    }

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
