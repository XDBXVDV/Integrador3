package com.integrador.toishan.controller;



import com.integrador.toishan.dto.createDTO.ClienteCreateDto;
import com.integrador.toishan.dto.updateDTO.ClienteUpdateDto;
import com.integrador.toishan.model.Cliente;
import com.integrador.toishan.service.ClienteDetalleDtoService;
import com.integrador.toishan.service.ClienteDtoService;
import com.integrador.toishan.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteDtoService clienteDtoService;
    @Autowired
    private ClienteDetalleDtoService detalleService;


    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(clienteDtoService.listaCliente());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id){
        return ResponseEntity.ok(detalleService.obtenerDetalle(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<Cliente> crear(@RequestBody ClienteCreateDto dto) {
        return ResponseEntity.ok(clienteService.crearCliente(dto));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteUpdateDto dto) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, dto));
    }
}

