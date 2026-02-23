package com.integrador.toishan.service;


import com.integrador.toishan.dto.modelDTO.ProductoDTO;
import com.integrador.toishan.model.Producto;

import com.integrador.toishan.repo.ProductoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoDtoService {
    @Autowired
    private ProductoRepo productoRepo;

    public List<ProductoDTO> listaProducto(){
        List<Producto> productos=  productoRepo.findAll();
        return productos.stream().map(this::mapToDTO).toList();
    }

    private ProductoDTO mapToDTO(Producto producto){
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setIdProducto(producto.getIdproducto());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        productoDTO.setCondicion(producto.getCondicion().toString());
        productoDTO.setCategoria(producto.getCategoria().getNombre());
        productoDTO.setMarca(producto.getMarca().getNombre());
        productoDTO.setEstado(producto.getEstado().toString());
        return productoDTO;
    }


}
