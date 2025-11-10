package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Compra;
import com.example.demo.service.CompraService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/compras")
@Tag(name = "Compras", description = "Operaciones relacionadas con las compras realizadas por los usuarios")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @GetMapping
    @Operation(summary = "Listar todas las compras", description = "Obtiene una lista de todas las compras registradas en el sistema.")
    public ResponseEntity<List<Compra>> findAll() {
        List<Compra> compras = compraService.findAll();

        // Forzar carga de relaciones para evitar problemas con lazy loading
        compras.forEach(compra -> {
            if (compra.getCompraEntradas() != null) compra.getCompraEntradas().size();
            if (compra.getUsuario() != null) compra.getUsuario().getEmail();
        });

        if (compras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar compra por ID", description = "Obtiene la información de una compra específica por su ID.")
    public ResponseEntity<Compra> findById(@PathVariable Long id) {
        Compra compra = compraService.findById(id);
        if (compra == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(compra);
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva compra", description = "Crea un nuevo registro de compra en el sistema.")
    public ResponseEntity<Compra> save(@RequestBody Compra compra) {
        Compra created = compraService.save(compra);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una compra", description = "Modifica todos los campos de una compra existente.")
    public ResponseEntity<Compra> update(@PathVariable Long id, @RequestBody Compra compra) {
        Compra updated = compraService.update(id, compra);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una compra", description = "Actualiza algunos campos de una compra sin modificar los demás.")
    public ResponseEntity<Compra> patch(@PathVariable Long id, @RequestBody Compra compra) {
        Compra patched = compraService.patch(id, compra);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una compra", description = "Elimina una compra específica del sistema por su ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Compra compra = compraService.findById(id);
        if (compra == null) {
            return ResponseEntity.notFound().build();
        }
        compraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
