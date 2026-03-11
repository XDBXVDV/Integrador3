package com.integrador.toishan.controller;


import com.integrador.toishan.dto.createDTO.VentaRequestDTO;
import com.integrador.toishan.model.Venta;
import com.integrador.toishan.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producto/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

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
}
