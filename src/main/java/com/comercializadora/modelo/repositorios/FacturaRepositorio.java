package com.comercializadora.modelo.repositorios;
import com.comercializadora.modelo.entidades.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FacturaRepositorio extends JpaRepository<Factura, Long> {
    List<Factura> findByClienteContainingIgnoreCase(String cliente);
    @Query("SELECT COALESCE(SUM(f.total),0) FROM Factura f")
    Double sumaTotalFacturas();
}
