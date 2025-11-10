package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.CompraEntrada;
import com.example.demo.model.Entrada;
import com.example.demo.repository.CompraEntradaRepository;
import com.example.demo.repository.EntradaRepository;
import com.example.demo.service.EntradaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/entradas")
@Tag(name = "Entradas", description = "Operaciones relacionadas con las entradas del sistema")
public class EntradaController {

    private final EntradaRepository entradaRepository;
    private final EntradaService entradaService;

    @Autowired
    private CompraEntradaRepository compraEntradaRepository;

    public EntradaController(EntradaRepository entradaRepository, EntradaService entradaService) {
        this.entradaRepository = entradaRepository;
        this.entradaService = entradaService;
    }

    // -----------------------------------------------
    // LISTAR TODAS
    // -----------------------------------------------
    @GetMapping
    @Operation(summary = "Listar todas las entradas", description = "Obtiene una lista de todas las entradas disponibles o registradas.")
    public ResponseEntity<List<Entrada>> findAll() {
        List<Entrada> entradas = entradaService.findAll();
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    // -----------------------------------------------
    // BUSCAR POR ID
    // -----------------------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Buscar entrada por ID", description = "Obtiene la información de una entrada específica por su ID.")
    public ResponseEntity<Entrada> findById(@PathVariable Long id) {
        Entrada entrada = entradaService.findById(id);
        if (entrada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entrada);
    }

    // -----------------------------------------------
    // CREAR NUEVA ENTRADA
    // -----------------------------------------------
    @PostMapping
    @Operation(summary = "Crear una nueva entrada", description = "Registra una nueva entrada dentro del sistema.")
    public ResponseEntity<Entrada> save(@RequestBody Entrada entrada) {
        Entrada created = entradaService.save(entrada);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // -----------------------------------------------
    // PATCH: MARCAR COMO USADA (desde CompraEntrada)
    // -----------------------------------------------
    @PatchMapping("/usar/{codigoQR}")
    @Operation(summary = "Marcar entrada como usada", description = "Valida una entrada por su código QR y la marca como usada.")
    public ResponseEntity<?> marcarEntradaUsada(@PathVariable String codigoQR) {
        Optional<CompraEntrada> compraEntradaOpt = compraEntradaRepository.findByCodigoQR(codigoQR);

        if (compraEntradaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Entrada no encontrada"));
        }

        CompraEntrada compraEntrada = compraEntradaOpt.get();

        if ("ocupada".equalsIgnoreCase(compraEntrada.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "La entrada ya fue utilizada"));
        }

        compraEntrada.setEstado("ocupada");
        compraEntradaRepository.save(compraEntrada);

        return ResponseEntity.ok(Map.of("mensaje", "Entrada validada correctamente"));
    }

    // -----------------------------------------------
    // ELIMINAR ENTRADA
    // -----------------------------------------------
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
