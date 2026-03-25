package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.RequerimientoDTO;
import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.EmpleadoRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.repo.RequerimientoRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequerimientoService {

    @Autowired private RequerimientoRepo repo;
    @Autowired private ProductoRepo productoRepo;
    @Autowired private EmpleadoRepo empleadoRepo;

    @Transactional
    public void registrarRequerimientosMasivos(List<RequerimientoDTO> listaDTO) {
        for (RequerimientoDTO dto : listaDTO) {

            if (dto.getIdProducto() == null) {
                throw new RuntimeException("Error: ID de Producto llegó nulo del frontend");
            }
            if (dto.getIdEmpleadoAlmacen() == null) {
                throw new RuntimeException("Error: ID de Empleado llegó nulo. Revise el LocalStorage");
            }

            Producto producto = productoRepo.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + dto.getIdProducto()));

            Empleado empleado = empleadoRepo.findById(dto.getIdEmpleadoAlmacen())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado: " + dto.getIdEmpleadoAlmacen()));

            Requerimiento req = new Requerimiento();
            req.setProducto(producto);
            req.setEmpleadoAlmacen(empleado);
            req.setCantidadSugerida(dto.getCantidadSugerida());
            req.setEstado(EstadoRequerimiento.PENDIENTE);
            req.setPrioridad(producto.getStock() == 0 ? Prioridad.CRITICA : Prioridad.ALTA);

            repo.save(req);
        }
    }

    public List<Requerimiento> obtenerPendientes() {
        return repo.findByEstado(EstadoRequerimiento.PENDIENTE);
    }

    @Transactional
    public void cambiarEstado(Long idRequerimiento, EstadoRequerimiento nuevoEstado) {
        Requerimiento requerimiento = repo.findById(idRequerimiento)
                .orElseThrow(() -> new RuntimeException("Requerimiento no encontrado con ID: " + idRequerimiento));
        requerimiento.setEstado(nuevoEstado);
        repo.save(requerimiento);

        System.out.println("Requerimiento " + idRequerimiento + " cambiado a estado: " + nuevoEstado);
    }

}