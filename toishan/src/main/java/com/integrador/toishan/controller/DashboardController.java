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
            // 1. KPIs de las tarjetas
            stats.put("totalVentasMes", ventaRepo.sumarVentasMesActual());
            stats.put("totalComprasMes", compraRepo.sumarComprasMesActual());
            stats.put("productosCriticos", productoRepo.countByCondicionIn(Arrays.asList(Condicion.Agotado, Condicion.Stock_bajo)));

            // 2. Gráfico Comparativo (Ventas vs Compras) - Sincronización de Fechas
            List<Object[]> ventasRaw = ventaRepo.obtenerVentasUltimaSemana();
            List<Object[]> comprasRaw = compraRepo.obtenerComprasUltimaSemana();

            Map<String, Double> ventasMap = new LinkedHashMap<>();
            Map<String, Double> comprasMap = new LinkedHashMap<>();

            for (Object[] v : ventasRaw) ventasMap.put(v[0].toString(), Double.valueOf(v[1].toString()));
            for (Object[] c : comprasRaw) comprasMap.put(c[0].toString(), Double.valueOf(c[1].toString()));

            Set<String> todasLasFechas = new TreeSet<>(ventasMap.keySet());
            todasLasFechas.addAll(comprasMap.keySet());

            List<String> etiquetasDias = new ArrayList<>();
            List<Double> datosVentas = new ArrayList<>();
            List<Double> datosCompras = new ArrayList<>();

            for (String fecha : todasLasFechas) {
                etiquetasDias.add(fecha);
                datosVentas.add(ventasMap.getOrDefault(fecha, 0.0));
                datosCompras.add(comprasMap.getOrDefault(fecha, 0.0));
            }
            stats.put("etiquetasDias", etiquetasDias);
            stats.put("datosVentas", datosVentas);
            stats.put("datosCompras", datosCompras);

            // 3. Gráfico Circular (Categorías)
            List<Object[]> catRaw = ventaRepo.obtenerVentasPorCategoria();
            List<String> etiquetasCat = new ArrayList<>();
            List<Double> datosCat = new ArrayList<>();
            for (Object[] fila : catRaw) {
                etiquetasCat.add(fila[0].toString());
                datosCat.add(Double.valueOf(fila[1].toString()));
            }
            stats.put("etiquetasCat", etiquetasCat);
            stats.put("datosCat", datosCat);

            // 4. Tabla Top Productos
            List<Object[]> topRaw = ventaRepo.obtenerTopProductosDashboard();
            List<Map<String, Object>> topProcesados = new ArrayList<>();
            for (Object[] f : topRaw) {
                Map<String, Object> p = new HashMap<>();
                p.put("nombre", f[0]);
                p.put("cantidad", f[1]);
                p.put("total", f[2]);
                topProcesados.add(p);
            }
            stats.put("topProductos", topProcesados);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}