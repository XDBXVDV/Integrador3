package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.ProductoCreateDTO;
import com.integrador.toishan.dto.updateDTO.ProductoUpdateDTO;
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

    public Producto crear(ProductoCreateDTO dto) {

        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setPrecio(dto.getPrecio());
        p.setStock(dto.getStock());
        p.setStockMinimo(dto.getStockMinimo());
        p.setCategoria(categoriaRepo.findById(dto.getIdCategoria()).orElseThrow());
        p.setMarca(marcaRepo.findById(dto.getIdMarca()).orElseThrow());
        p.setEstado(Estado.Activo);

        return productoRepo.save(p);
    }

    public void actualizarStock(Long idProducto, int cantidad) {
        Producto p = productoRepo.findById(idProducto).orElseThrow();
        p.setStock(p.getStock() + cantidad);
        productoRepo.save(p);
    }

    public Producto actualizarProducto(Long idProducto, ProductoUpdateDTO dto) {

        Producto producto = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Marca marca = marcaRepo.findById(dto.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        if (dto.getPrecio() == null || dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        if (dto.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        if (dto.getStockMinimo() < 0) {
            throw new RuntimeException("El stock mínimo no puede ser negativo");
        }

        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }


        producto.setCategoria(categoria);
        producto.setNombre(dto.getNombre());
        producto.setMarca(marca);
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setEstado(dto.getEstado());


        producto.setCondicion(calcularCondicionStock(
                dto.getStock(),
                dto.getStockMinimo()
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

