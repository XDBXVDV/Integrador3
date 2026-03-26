package com.integrador.toishan.controller;

import com.integrador.toishan.model.Condicion;
import com.integrador.toishan.repo.PedidoCompraRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.repo.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private VentaRepo ventaRepo;
    @Autowired
    private PedidoCompraRepo compraRepo;
    @Autowired
    private ProductoRepo productoRepo;

    @GetMapping("/resumen")
    public ResponseEntity<?> obtenerResumen() {
        Map<String, Object> stats = new HashMap<>();
        try {
            Double ventasMes = ventaRepo.sumarVentasMesActual();
            Double comprasMes = compraRepo.sumarComprasMesActual();

            // Debug para consola
            System.out.println("Ventas recuperadas: " + ventasMes);
            System.out.println("Compras recuperadas: " + comprasMes);

            stats.put("totalVentasMes", ventasMes);
            stats.put("totalComprasMes", comprasMes);
            stats.put("productosCriticos", productoRepo.countByCondicionIn(Arrays.asList(Condicion.Agotado, Condicion.Stock_bajo)));

            List<Object[]> ventasSemanales = ventaRepo.obtenerVentasUltimaSemana();
            List<String> etiquetasDias = new ArrayList<>();
            List<Double> datosVentas = new ArrayList<>();

            for (Object[] fila : ventasSemanales) {
                etiquetasDias.add(fila[0].toString());
                datosVentas.add(Double.valueOf(fila[1].toString()));
            }

            stats.put("etiquetasDias", etiquetasDias);
            stats.put("datosVentas", datosVentas);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}