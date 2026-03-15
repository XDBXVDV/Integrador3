package com.integrador.toishan.service;

import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class FacturaCompraService {

    @Autowired
    private FacturaCompraRepo facturaRepo;

    @Autowired
    private OrdenRepo ordenRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Transactional
    public void registrarFacturaYSubirStock(Long idOrden, String serie, String correlativo, MultipartFile archivo) throws IOException {

        OrdenCompra orden = ordenRepo.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (orden.getEstado() == EstadoOrden.FACTURADA) {
            throw new RuntimeException("Esta orden ya ha sido facturada.");
        }

        FacturaCompra factura = new FacturaCompra();
        factura.setOrdenCompra(orden);
        factura.setSerieFactura(serie);
        factura.setCorrelativoFactura(correlativo);
        factura.setTotalPagado(orden.getTotal());

        if (archivo != null && !archivo.isEmpty()) {
            String rutaPdf = guardarFacturaPdf(archivo);
            factura.setPdfUrl(rutaPdf);
        }

        facturaRepo.save(factura);

        orden.getCotizacion().getDetalles().forEach(det -> {
            Producto p = det.getProducto();
            p.setStock(p.getStock() + det.getCantidad());
            productoRepo.save(p);
        });

        orden.setEstado(EstadoOrden.FACTURADA);
        ordenRepo.save(orden);
    }

    private String guardarFacturaPdf(MultipartFile archivo) throws IOException {
        if(archivo == null || archivo.isEmpty()){
            return null;
        }

        // Validar extensión
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf(".")).toLowerCase();
        if(!extension.equals(".pdf")){
            throw new RuntimeException("Solo se permiten archivos PDF");
        }

        // Generar nombre único
        String nombreArchivo = UUID.randomUUID().toString() + extension;

        // Carpeta al lado de tus productos
        String carpeta = "src/main/resources/static/docs/facturas/";
        File directorio = new File(carpeta);
        if(!directorio.exists()){
            directorio.mkdirs();
        }

        Path ruta = Paths.get(carpeta + nombreArchivo);
        Files.write(ruta, archivo.getBytes());

        // Retorna la URL relativa para el navegador
        return "/docs/facturas/" + nombreArchivo;
    }

    private String guardarArchivoFisico(MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo de factura es obligatorio.");
        }

        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || !nombreOriginal.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("Formato no permitido. Solo se aceptan archivos .pdf");
        }
        Path rutaDirectorio = Paths.get("Uploads", "Facturas").toAbsolutePath().normalize();
        if (!Files.exists(rutaDirectorio)) {
            Files.createDirectories(rutaDirectorio);
        }
        String nombreArchivo = "FACT_" + UUID.randomUUID().toString() + ".pdf";
        Path rutaDestino = rutaDirectorio.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        return "/Uploads/Facturas/" + nombreArchivo;
    }

    public List<FacturaCompra> listarTodas() {
        return facturaRepo.findAll();
    }
}