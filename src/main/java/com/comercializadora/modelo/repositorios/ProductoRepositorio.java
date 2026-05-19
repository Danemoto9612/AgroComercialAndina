package com.comercializadora.modelo.repositorios;
import com.comercializadora.modelo.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);
    List<Producto> findByTipoProductoIgnoreCase(String tipo);
    @Query("SELECT p FROM Producto p WHERE p.stockProducto > 0")
    List<Producto> findDisponibles();
}
