package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.VentaDTO;
import com.integrador.toishan.model.Venta;
import com.integrador.toishan.repo.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class VentaDtoService {
    @Autowired
    private VentaRepo ventaRepo;

    public List<VentaDTO> getListVenta()
    {
        List<Venta> ventas=ventaRepo.findAll();
        return ventas.stream().map(this::mapToDTO).toList();
    }


    private VentaDTO mapToDTO(Venta v)
    {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(v.getIdVenta());
        dto.setNombreCliente(v.getCliente().getNombre()+" "+v.getCliente().getApellido());
        if (v.getFechaventa() != null) {
            dto.setFechaVenta(v.getFechaventa().toString());
        }
        dto.setTotal(v.getTotal());
        dto.setMetodoPago(v.getMetodoPago().toString());
        dto.setEstado(v.getEstado().toString());
        return dto;
    }
}
