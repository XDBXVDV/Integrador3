package com.integrador.toishan.repo;


import com.integrador.toishan.model.EstadoPedidoCompra;
import com.integrador.toishan.model.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoCompraRepo extends JpaRepository<PedidoCompra,Long> {
    List<PedidoCompra> findByEstado(EstadoPedidoCompra estado);

    @Query(value = "SELECT COALESCE(SUM(c.total_cotizado), 0.0) FROM pedidos_compra p " +
            "JOIN cotizaciones c ON p.id_pedido_compra = c.id_pedido_compra " +
            "WHERE MONTH(p.fecha) = MONTH(CURRENT_DATE()) " +
            "AND YEAR(p.fecha) = YEAR(CURRENT_DATE())", nativeQuery = true)
    Double sumarComprasMesActual();

    @Query(value = "SELECT DATE_FORMAT(p.fecha, '%d/%m') as dia, SUM(c.total_cotizado) as monto " +
            "FROM pedidos_compra p " +
            "JOIN cotizaciones c ON p.id_pedido_compra = c.id_pedido_compra " +
            "WHERE p.fecha >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) " +
            "GROUP BY dia, p.fecha ORDER BY p.fecha ASC", nativeQuery = true)
    List<Object[]> obtenerComprasUltimaSemana();
}
