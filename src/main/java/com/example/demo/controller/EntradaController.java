package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Entrada;
import com.example.demo.service.EntradaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/entradas")
@Tag(name = "Entradas", description = "Operaciones relacionadas con las entradas del sistema")
public class EntradaController {

    @Autowired
    private EntradaService entradaService;

    @GetMapping
    @Operation(summary = "Listar todas las entradas", description = "Obtiene una lista de todas las entradas disponibles o registradas.")
    public ResponseEntity<List<Entrada>> findAll() {
        List<Entrada> entradas = entradaService.findAll();
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar entrada por ID", description = "Obtiene la información de una entrada específica por su ID.")
    public ResponseEntity<Entrada> findById(@PathVariable Long id) {
        Entrada entrada = entradaService.findById(id);
        if (entrada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entrada);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva entrada", description = "Registra una nueva entrada dentro del sistema.")
    public ResponseEntity<Entrada> save(@RequestBody Entrada entrada) {
        Entrada created = entradaService.save(entrada);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una entrada", description = "Modifica todos los campos de una entrada existente.")
    public ResponseEntity<Entrada> update(@PathVariable Long id, @RequestBody Entrada entrada) {
        Entrada updated = entradaService.update(id, entrada);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una entrada", description = "Actualiza algunos campos de una entrada sin modificar los demás.")
    public ResponseEntity<Entrada> patch(@PathVariable Long id, @RequestBody Entrada entrada) {
        Entrada patched = entradaService.patch(id, entrada);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una entrada", description = "Elimina una entrada específica del sistema por su ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Entrada entrada = entradaService.findById(id);
        if (entrada == null) {
            return ResponseEntity.notFound().build();
        }
        entradaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
