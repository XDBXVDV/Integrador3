package com.integrador.toishan.controller;

import com.integrador.toishan.dto.modelDTO.RequerimientoDTO;
import com.integrador.toishan.model.EstadoRequerimiento;
import com.integrador.toishan.model.Requerimiento;
import com.integrador.toishan.service.RequerimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requerimientos")
@CrossOrigin(origins = "*")
public class RequerimientoController {

    @Autowired
    private RequerimientoService requerimientoService;

    @PostMapping("/registrar-masivo")
    public ResponseEntity<?> registrarMasivo(@RequestBody List<RequerimientoDTO> listaDTO) {
        // Si esto imprime "null", el problema es el nombre de la variable en el JS
        for(RequerimientoDTO d : listaDTO) {
            System.out.println("JSON mapeado -> ID: " + d.getIdProducto() + ", Cant: " + d.getCantidadSugerida());
        }

        try {
            requerimientoService.registrarRequerimientosMasivos(listaDTO);
            return ResponseEntity.ok("Requerimientos registrados");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Requerimiento>> listarPendientes() {
        return ResponseEntity.ok(requerimientoService.obtenerPendientes());
    }

    @PutMapping("/actualizar-estado/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam EstadoRequerimiento estado) {
        try {
            requerimientoService.cambiarEstado(id, estado);
            return ResponseEntity.ok("Estado del requerimiento actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}