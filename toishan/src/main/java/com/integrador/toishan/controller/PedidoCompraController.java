package com.integrador.toishan.controller;

import com.integrador.toishan.dto.modelDTO.RegistroPedidoDTO;
import com.integrador.toishan.model.EstadoPedidoCompra;
import com.integrador.toishan.model.PedidoCompra;
import com.integrador.toishan.service.PedidoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos-compra")
@CrossOrigin(origins = "*")
public class PedidoCompraController {

    @Autowired private PedidoCompraService pedidoService;

    @GetMapping("/todos")
    public ResponseEntity<List<PedidoCompra>> listarTodo() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<PedidoCompra>> listarPendientes() {
        return ResponseEntity.ok(pedidoService.listarPedidosPendientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompra> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarCompra(@RequestBody RegistroPedidoDTO dto) {
        try {
            PedidoCompra nuevoPedido = pedidoService.registrarRequerimiento(dto);
            return ResponseEntity.ok(nuevoPedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestParam("nuevoEstado") EstadoPedidoCompra nuevoEstado) {
        try {
            pedidoService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok("Estado actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}