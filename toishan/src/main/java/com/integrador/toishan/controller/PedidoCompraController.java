package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.ConversionPedidoDTO;
import com.integrador.toishan.dto.modelDTO.RegistroPedidoDTO;
import com.integrador.toishan.model.EstadoPedidoCompra;
import com.integrador.toishan.model.PedidoCompra;
import com.integrador.toishan.model.Producto;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.service.PedidoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos-compra")
@CrossOrigin(origins = "*")
public class PedidoCompraController {

    @Autowired
    private PedidoCompraService pedidoService;

    @Autowired
    private ProductoRepo productoRepo;

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

    @GetMapping("/faltantes")
    public ResponseEntity<List<Producto>> listarFaltantes() {
        return ResponseEntity.ok(productoRepo.findProductosFaltantes());
    }

    @PostMapping("/convertir-desde-requerimientos")
    public ResponseEntity<?> convertir(@RequestBody ConversionPedidoDTO datos) {
        try {
            // Ahora los datos ya vienen con el tipo correcto (Long y List<Long>)
            pedidoService.convertirRequerimientosAPedido(
                    datos.getIdsRequerimientos(),
                    datos.getIdProveedor(),
                    datos.getIdEmpleadoLogistica()
            );
            return ResponseEntity.ok("Pedido Creado Correctamente");
        } catch (Exception e) {
            e.printStackTrace(); // Revisa la consola de tu IDE si esto falla
            return ResponseEntity.badRequest().body("Error en la conversión: " + e.getMessage());
        }
    }

}