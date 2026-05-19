package com.comercializadora.modelo.repositorios;
import com.comercializadora.modelo.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreClienteContainingIgnoreCase(String nombre);
    List<Cliente> findByTipoClienteIgnoreCase(String tipo);
}
