package com.integrador.toishan.controller;


import com.integrador.toishan.dto.createDTO.DetalleVentaDTO;
import com.integrador.toishan.dto.createDTO.VentaRequestDTO;
import com.integrador.toishan.model.Venta;
import com.integrador.toishan.service.VentaDtoService;
import com.integrador.toishan.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaDtoService ventaDtoService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody VentaRequestDTO request) {
        try {

            Venta nuevaVenta = ventaService.registrarVenta(request);


            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado en el servidor");
        }
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Venta>> listarPorCliente(@PathVariable Long idCliente) {
        // En tu VentaRepository: List<Venta> findByCliente_IdClienteOrderByIdVentaDesc(Integer idCliente);
        List<Venta> ventas = ventaService.listarVentasPorCliente(idCliente);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/detalle/{idVenta}")
    public ResponseEntity<List<DetalleVentaDTO>> obtenerDetalleVenta(@PathVariable Integer idVenta) {
        List<DetalleVentaDTO> detalles = ventaService.listarDetallesPorVenta(idVenta);
        return ResponseEntity.ok(detalles);
    }

    @PutMapping("/anular/{idVenta}")
    public ResponseEntity<?> anular(@PathVariable Long idVenta) {
        try {
            ventaService.anularVenta(idVenta);
            return ResponseEntity.ok("Venta #" + idVenta + " anulada y stock restaurado.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la anulación");
        }
    }
    @GetMapping("/listar")
    public ResponseEntity<?> listarVentas() {
        return ResponseEntity.ok(ventaDtoService.getListVenta());
    }

}
