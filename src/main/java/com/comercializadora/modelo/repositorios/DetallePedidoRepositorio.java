package com.comercializadora.modelo.repositorios;
import com.comercializadora.modelo.entidades.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DetallePedidoRepositorio extends JpaRepository<DetallePedido, Long> {}
