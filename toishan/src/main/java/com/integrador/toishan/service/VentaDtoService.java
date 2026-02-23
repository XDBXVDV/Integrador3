package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.DetalleVentaDTO;
import com.integrador.toishan.dto.modelDTO.VentaDTO;
import com.integrador.toishan.model.DetalleVenta;
import com.integrador.toishan.model.Venta;
import com.integrador.toishan.repo.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VentaDtoService {
    @Autowired
    private VentaRepo ventaRepo;

    public List<VentaDTO> ListarVentas(){
        List<Venta> ventas= ventaRepo.findAll();
        return ventas.stream().map(this::mapToDTO).toList();
    };

    private VentaDTO mapToDTO(Venta venta){
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setIdVenta(venta.getIdVenta());
        ventaDTO.setFechaVenta(venta.getFechaVenta());
        ventaDTO.setTotal(venta.getTotal());
        ventaDTO.setEstado(ventaDTO.getEstado());
        ventaDTO.setNombreCliente(venta.getCliente().getNombre()+" "+venta.getCliente().getApellido());
        ventaDTO.setNombreEmpleado(venta.getEmpleado().getNombre()+" "+venta.getEmpleado().getApellido());
        ventaDTO.setEstado(venta.getEstado().toString());
        List<DetalleVentaDTO> detallesDto= venta.getDetalles().stream().map(this::mapToDetalleDTO).toList();
        ventaDTO.setDetalles(detallesDto);
        return ventaDTO;
    }
    private DetalleVentaDTO mapToDetalleDTO(DetalleVenta detalleVenta){
        DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO();
        detalleVentaDTO.setProducto(detalleVenta.getProducto().getNombre());
        detalleVentaDTO.setCantidad(detalleVenta.getCantidad());
        detalleVentaDTO.setPrecioUnitario(detalleVenta.getPrecioUnitario());
        detalleVentaDTO.setSubtotal(detalleVenta.getSubtotal());
        return detalleVentaDTO;
    }
}
