package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.DetalleDevolucionDTO;

import com.integrador.toishan.dto.modelDTO.DevolucionDTO;
import com.integrador.toishan.model.DetalleDevolucion;
import com.integrador.toishan.model.Devolucion;
import com.integrador.toishan.repo.DevolucionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DevolucionDtoService {
    @Autowired
    private DevolucionRepo devolucionRepo;

    public List<DevolucionDTO> findAll(){
        List<Devolucion> devolucions = devolucionRepo.findAll();
        return devolucions.stream().map(this::maptoDto).toList();
    }

    private DevolucionDTO maptoDto(Devolucion devolucion){
        DevolucionDTO dto = new DevolucionDTO();
        dto.setIdDevolucion(devolucion.getIdDevolucion());
        dto.setFechaDevolucion(devolucion.getFechaDevolucion());
        dto.setTotalDevuelto(devolucion.getTotalDevuelto());
        dto.setEstado(devolucion.getEstado().toString());
        dto.setIdVenta(dto.getIdVenta());
        List<DetalleDevolucionDTO> detallesDto= devolucion.getDetalles().stream().map(this::maptoDetalleDto).toList();
        dto.setDetalles(detallesDto);
        return dto;
    }

    private DetalleDevolucionDTO maptoDetalleDto(DetalleDevolucion detalle){
        DetalleDevolucionDTO dto = new DetalleDevolucionDTO();
        dto.setProducto(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
