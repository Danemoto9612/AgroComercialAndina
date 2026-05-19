package com.comercializadora.modelo.repositorios;
import com.comercializadora.modelo.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstadoPedidoIgnoreCase(String estado);
    List<Pedido> findByClienteIdCliente(Long idCliente);
}
