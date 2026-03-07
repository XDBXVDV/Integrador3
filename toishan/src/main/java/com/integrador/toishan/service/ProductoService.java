package com.integrador.toishan.service;


import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.CategoriaRepo;
import com.integrador.toishan.repo.MarcaRepo;
import com.integrador.toishan.repo.ProductoRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Autowired
    private MarcaRepo marcaRepo;


    public Producto findById(Long id){
        return productoRepo.findById(id).orElse(null);
    }


    public Collection<Producto> findAll(){
        return productoRepo.findAll();
    }


    public Producto crear(Producto producto1) {

        Producto p = new Producto();


        p.setNombre(producto1.getNombre());
        p.setDescripcion(producto1.getDescripcion());
        p.setPrecio(producto1.getPrecio());
        p.setStock(producto1.getStock());
        p.setStockMinimo(producto1.getStockMinimo());
        p.setImagen(producto1.getImagen());


        if (producto1.getCategoria() != null) {
            p.setCategoria(
                    categoriaRepo.findById(producto1.getCategoria().getIdCategoria())
                            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"))
            );
        }


        if (producto1.getMarca() != null) {
            p.setMarca(
                    marcaRepo.findById(producto1.getMarca().getIdMarca())
                            .orElseThrow(() -> new RuntimeException("Marca no encontrada"))
            );
        }


        p.setEstado(Estado.Activo);


        p.setCondicion(calcularCondicionStock(
                producto1.getStock(),
                producto1.getStockMinimo()
        ));

        return productoRepo.save(p);
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
        producto.setMarca(marca);
        producto.setNombre(producto1.getNombre());
        producto.setDescripcion(producto1.getDescripcion());
        producto.setPrecio(producto1.getPrecio());
        producto.setStock(producto1.getStock());
        producto.setStockMinimo(producto1.getStockMinimo());
        producto.setImagen(producto1.getImagen());
        producto.setEstado(producto1.getEstado());



        producto.setCondicion(
                calcularCondicionStock(
                        producto1.getStock(),
                        producto1.getStockMinimo()
                )
        );

        return productoRepo.save(producto);
    }



    public void desactivar(Long idProducto) {

        Producto producto = findById(idProducto);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        if (producto.getEstado().equals(Estado.Activo)) {
            producto.setEstado(Estado.Inactivo);
            productoRepo.save(producto);
        } else {
            throw new RuntimeException("El producto ya está inactivo");
        }
    }



    public void reactivar(Long idProducto) {

        Producto producto = findById(idProducto);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        if (producto.getEstado().equals(Estado.Inactivo)) {
            producto.setEstado(Estado.Activo);
            productoRepo.save(producto);
        } else {
            throw new RuntimeException("El producto ya está activo");
        }
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