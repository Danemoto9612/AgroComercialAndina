package com.comercializadora.modelo.repositorios;
import com.comercializadora.modelo.entidades.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DetalleFacturaRepositorio extends JpaRepository<DetalleFactura, Long> {}
