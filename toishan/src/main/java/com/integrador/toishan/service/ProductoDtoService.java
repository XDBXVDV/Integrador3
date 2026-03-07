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

        ProductoDTO dto = new ProductoDTO();

        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagen(producto.getImagen());
        dto.setCategoria(producto.getCategoria().getNombre());
        dto.setMarca(producto.getMarca().getNombre());
        dto.setCondicion(producto.getCondicion().name());
        dto.setEstado(producto.getEstado().name());
        return dto;
    }


}
