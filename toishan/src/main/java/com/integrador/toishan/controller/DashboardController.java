package com.integrador.toishan.controller;

import com.integrador.toishan.model.Condicion;
import com.integrador.toishan.repo.PedidoCompraRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.repo.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/backup")
    public ResponseEntity<String> ejecutarBackup() {
        try {
            // 1. Detectar ruta de MySQL (el código que ya tienes)
            String[] posiblesRutas = {
                    "C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe",
                    "C:/Archivos de programa/MySQL/MySQL Server 8.0/bin/mysqldump.exe"
            };
            String mysqldumpPath = "";
            for (String ruta : posiblesRutas) {
                if (new File(ruta).exists()) { mysqldumpPath = ruta; break; }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String timestamp = LocalDateTime.now().format(formatter);
            String nombreArchivo = "toishan_backup_" + timestamp + ".sql";

            File carpeta = new File(System.getProperty("user.dir"), "backups");
            if (!carpeta.exists()) carpeta.mkdir();
            File archivoDestino = new File(carpeta, nombreArchivo);

            // 3. Ejecutar Backup con ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(
                    mysqldumpPath,
                    "-u", "root",
                    "-p1234",
                    "toishan",
                    "-r", archivoDestino.getAbsolutePath()
            );

            pb.redirectErrorStream(true);
            Process p = pb.start();

            if (p.waitFor() == 0) {
                return ResponseEntity.ok("✅ Nuevo respaldo creado: " + nombreArchivo);
            } else {
                String errorMsg = new String(p.getInputStream().readAllBytes());
                return ResponseEntity.status(500).body("❌ Error de MySQL: " + errorMsg);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error de Sistema: " + e.getMessage());
        }
    }

    @GetMapping("/listar-backups")
    public ResponseEntity<List<String>> listarBackups() {
        try {
            File carpeta = new File(System.getProperty("user.dir"), "backups");
            if (!carpeta.exists()) return ResponseEntity.ok(new ArrayList<>());
            String[] archivos = carpeta.list((dir, name) -> name.endsWith(".sql"));
            List<String> lista = (archivos != null) ? Arrays.asList(archivos) : new ArrayList<>();
            lista.sort(Collections.reverseOrder());

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/eliminar-backup/{nombre}")
    public ResponseEntity<String> eliminarBackup(@PathVariable String nombre) {
        try {
            File archivo = new File(System.getProperty("user.dir") + File.separator + "backups", nombre);
            if (archivo.exists() && archivo.delete()) {
                return ResponseEntity.ok("Archivo eliminado correctamente.");
            } else {
                return ResponseEntity.status(404).body("No se pudo encontrar el archivo.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/restaurar-backup/{nombre}")
    public ResponseEntity<String> restaurarBackup(@PathVariable String nombre) {
        try {
            // 1. Localizar mysql.exe (el restaurador)
            String[] posiblesRutas = {
                    "C:/Program Files/MySQL/MySQL Server 8.0/bin/mysql.exe",
                    "C:/Archivos de programa/MySQL/MySQL Server 8.0/bin/mysql.exe"
            };
            String mysqlPath = "";
            for (String ruta : posiblesRutas) {
                if (new File(ruta).exists()) { mysqlPath = ruta; break; }
            }

            File archivoSource = new File(System.getProperty("user.dir") + File.separator + "backups", nombre);
            if (!archivoSource.exists()) return ResponseEntity.status(404).body("El archivo no existe.");

            ProcessBuilder pb = new ProcessBuilder(
                    mysqlPath,
                    "-u", "root",
                    "-p1234",
                    "toishan"
            );
            pb.redirectInput(archivoSource);
            Process p = pb.start();
            if (p.waitFor() == 0) {
                return ResponseEntity.ok("✅ Base de datos restaurada con éxito desde: " + nombre);
            } else {
                String errorMsg = new String(p.getErrorStream().readAllBytes());
                return ResponseEntity.status(500).body("❌ Error en restauración: " + errorMsg);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error de sistema: " + e.getMessage());
        }
    }

}