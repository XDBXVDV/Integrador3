package com.integrador.toishan.service;


import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.CategoriaRepo;
import com.integrador.toishan.repo.MarcaRepo;
import com.integrador.toishan.repo.ProductoRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Autowired
    private MarcaRepo marcaRepo;

    public Producto crear(Producto producto1) {

        Producto p = new Producto();
        p.setNombre(producto1.getNombre());
        p.setPrecio(producto1.getPrecio());
        p.setStock(producto1.getStock());
        p.setStockMinimo(producto1.getStockMinimo());
        if (producto1.getCategoria() != null) {
            p.setCategoria(categoriaRepo.findById(producto1.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada")));
        }

        // Validación de seguridad para Marca
        if (producto1.getMarca() != null) {
            p.setMarca(marcaRepo.findById(producto1.getMarca().getIdMarca())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada")));
        }
        p.setEstado(Estado.Activo);

        if (producto1.getStock()>producto1.getStockMinimo()) {
            p.setCondicion(Condicion.En_stock);
        } else if (producto1.getStock()==0) {
            p.setCondicion(Condicion.Agotado);
        }
        else if (producto1.getStock()<producto1.getStockMinimo()) {
            p.setCondicion(Condicion.Stock_bajo);
        }

        return productoRepo.save(p);
    }

    public void actualizarStock(Long idProducto, int cantidad) {
        Producto p = productoRepo.findById(idProducto).orElseThrow();
        p.setStock(p.getStock() + cantidad);
        productoRepo.save(p);
    }

    public Producto actualizarProducto(Long idProducto, Producto producto1) {

        Producto producto = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepo.findById(producto1.getCategoria().getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Marca marca = marcaRepo.findById(producto1.getMarca().getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        if (producto1.getPrecio() == null || producto1.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        if (producto1.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        if (producto1.getStockMinimo() < 0) {
            throw new RuntimeException("El stock mínimo no puede ser negativo");
        }

        if (producto1.getNombre() == null || producto1.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }


        producto.setCategoria(categoria);
        producto.setNombre(producto1.getNombre());
        producto.setMarca(marca);
        producto.setPrecio(producto1.getPrecio());
        producto.setStock(producto1.getStock());
        producto.setStockMinimo(producto1.getStockMinimo());
        producto.setEstado(producto1.getEstado());


        producto.setCondicion(calcularCondicionStock(
                producto1.getStock(),
                producto1.getStockMinimo()
        ));

        return productoRepo.save(producto);
    }


    private Condicion calcularCondicionStock(int stock, int stockMinimo) {
        if (stock == 0) {
            return Condicion.Agotado;
        }
        if (stock <= stockMinimo) {
            return Condicion.Stock_bajo;
        }
        return Condicion.En_stock;
    }

}

