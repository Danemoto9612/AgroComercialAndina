package com.comercializadora.modelo.repositorios;
import com.comercializadora.modelo.entidades.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface PagoRepositorio extends JpaRepository<Pago, Long> {
    List<Pago> findByEstadoPagoIgnoreCase(String estado);
    List<Pago> findByFechaPagoBetween(LocalDate inicio, LocalDate fin);
    @Query("SELECT COALESCE(SUM(p.montoPago),0) FROM Pago p WHERE UPPER(p.estadoPago) = 'COMPLETADO'")
    Double sumaTotalPagosCompletados();
}
