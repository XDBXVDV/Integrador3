package com.integrador.toishan.controller;


import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.CategoriaRepo;
import com.integrador.toishan.repo.MarcaRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.service.ProductoDtoService;
import com.integrador.toishan.service.ProductoService;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoDtoService productoDtoService;
    @Autowired
    private ProductoRepo repo;

    @Autowired
    private MarcaRepo marcaRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @GetMapping("/listar")
    public ResponseEntity<?> listarProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/listarDTO")
    public ResponseEntity<?> listarProductosDTO() {return ResponseEntity.ok(productoDtoService.listaProducto());}

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Long id) {

        Producto producto = productoService.findById(id);

        if (producto != null) {
            return ResponseEntity.ok(producto);
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(@RequestParam String nombre, @RequestParam String descripcion,
            @RequestParam BigDecimal precio, @RequestParam Integer stock, @RequestParam Integer stockMinimo,
            @RequestParam String estado, @RequestParam Long categoriaId, @RequestParam Long marcaId,
            @RequestParam(required = false) MultipartFile imagen
    ) throws IOException {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setStockMinimo(stockMinimo);
        p.setCondicion(calcularCondicion(stock, stockMinimo));
        p.setEstado(Estado.valueOf(estado));

        Categoria c = categoriaRepo.findById(categoriaId).orElse(null);
        Marca m = marcaRepo.findById(marcaId).orElse(null);

        p.setCategoria(c);
        p.setMarca(m);

        if(imagen != null && !imagen.isEmpty()){
            String rutaImagen = guardarImagen(imagen);
            p.setImagen(rutaImagen);
        }

        repo.save(p);

        return ResponseEntity.ok("Producto creado");
    }


    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id,@RequestParam String nombre,
            @RequestParam String descripcion,@RequestParam BigDecimal precio,@RequestParam Integer stock,
            @RequestParam Integer stockMinimo,@RequestParam String estado,@RequestParam Long categoriaId,
            @RequestParam Long marcaId, @RequestParam(required = false) MultipartFile imagen
    ) throws IOException {
        Producto p = repo.findById(id).orElse(null);
        if(p == null){
            return ResponseEntity.badRequest().body("Producto no existe");
        }
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setStockMinimo(stockMinimo);
        p.setCondicion(calcularCondicion(stock, stockMinimo));
        p.setEstado(Estado.valueOf(estado));

        Categoria c = categoriaRepo.findById(categoriaId).orElse(null);
        Marca m = marcaRepo.findById(marcaId).orElse(null);

        p.setCategoria(c);
        p.setMarca(m);

        if(imagen != null && !imagen.isEmpty()){

            if(p.getImagen() != null) {
                eliminarImagen(p.getImagen());
            }
            String rutaImagen = guardarImagen(imagen);
            p.setImagen(rutaImagen);
        }
        repo.save(p);

        return ResponseEntity.ok("Producto actualizado");
    }


    @PutMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivarProducto(@PathVariable Long id) {

        try {

            productoService.desactivar(id);

            return ResponseEntity.ok("Producto desactivado");

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }


    @PutMapping("/activar/{id}")
    public ResponseEntity<?> activarProducto(@PathVariable Long id) {
        try {
            productoService.reactivar(id);
            return ResponseEntity.ok("Producto activado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private String guardarImagen(MultipartFile imagen) throws IOException {
        if(imagen == null || imagen.isEmpty()){
            return null;
        }

        long maxSize = 5 * 1024 * 1024;
        if(imagen.getSize() > maxSize){
            throw new RuntimeException("La imagen supera el tamaño máximo de 5MB");
        }
        String nombreOriginal = imagen.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf(".")).toLowerCase();
        List<String> extensionesPermitidas = List.of(".jpg", ".jpeg", ".png", ".webp");
        if(!extensionesPermitidas.contains(extension)){
            throw new RuntimeException("Formato de imagen no permitido");
        }
        String nombreArchivo = UUID.randomUUID().toString() + extension;
        String carpeta = "src/main/resources/static/img/productos/";
        File directorio = new File(carpeta);
        if(!directorio.exists()){
            directorio.mkdirs();
        }
        Path ruta = Paths.get(carpeta + nombreArchivo);
        Files.write(ruta, imagen.getBytes());
        return "/img/productos/" + nombreArchivo;
    }

    private void eliminarImagen(String rutaImagen){
        if(rutaImagen == null) return;
        try{
            String rutaCompleta = "src/main/resources/static" + rutaImagen;
            Path path = Paths.get(rutaCompleta);
            Files.deleteIfExists(path);
        }catch(Exception e){
            System.out.println("No se pudo eliminar la imagen");
        }

    }
    private Condicion calcularCondicion(int stock, int stockMinimo){

        if(stock == 0){
            return Condicion.Agotado;
        }
        else if(stock <= stockMinimo){
            return Condicion.Stock_bajo;
        }
        else{
            return Condicion.En_stock;
        }

    }

    @GetMapping("/faltantes")
    public ResponseEntity<List<Producto>> listarFaltantes() {
        return ResponseEntity.ok(repo.findProductosFaltantes());
    }

    @GetMapping("/inventario-filtrado")
    public ResponseEntity<List<Producto>> listarInventario(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idMarca,
            @RequestParam(required = false) Estado estado
    ) {
        List<Producto> lista = productoService.filtrarInventario(nombre, idCategoria, idMarca, estado);
        return ResponseEntity.ok(lista);
    }

    // 2. Endpoint para el Reporte PDF
    @GetMapping("/inventario-reporte-pdf")
    public void exportarInventarioPdf(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idMarca,
            @RequestParam(required = false) Estado estado,
            HttpServletResponse response
    ) throws IOException {

        List<Producto> productos = productoService.filtrarInventario(nombre, idCategoria, idMarca, estado);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reporte_inventario.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate()); // Rotamos a horizontal para que quepa todo

        // Títulos
        document.add(new Paragraph("TOISHAN - REPORTE DE INVENTARIO").setBold().setFontSize(18));
        document.add(new Paragraph("Filtros aplicados: " +
                (nombre != null ? "Nombre: " + nombre + " | " : "") +
                (estado != null ? "Estado: " + estado : "Todos")));
        document.add(new Paragraph("\n"));

        // Definición de tabla (ID, Nombre, Categoría, Marca, Stock, Estado)
        Table table = new Table(new float[]{1, 4, 2, 2, 1, 1});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("ID");
        table.addHeaderCell("Producto");
        table.addHeaderCell("Categoría");
        table.addHeaderCell("Marca");
        table.addHeaderCell("Stock");
        table.addHeaderCell("Estado");

        for (Producto p : productos) {
            table.addCell(String.valueOf(p.getIdProducto()));
            table.addCell(p.getNombre());
            table.addCell(p.getCategoria().getNombre());
            table.addCell(p.getMarca().getNombre());
            table.addCell(String.valueOf(p.getStock()));
            table.addCell(p.getEstado().toString());
        }

        document.add(table);
        document.close();
    }
}