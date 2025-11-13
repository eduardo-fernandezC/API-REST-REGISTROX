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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


import com.example.demo.model.ImagenPerfil;
import com.example.demo.service.ImagenPerfilService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/imagenes")
@Tag(name = "Imagenes de Perfil", description = "Operaciones relacionadas con las imágenes de perfil de los usuarios")
public class ImagenPerfilController {

    @Autowired
    private ImagenPerfilService imagenPerfilService;

    @GetMapping
    @Operation(summary = "Listar todas las imágenes", description = "Obtiene una lista de todas las imágenes de perfil registradas.")
    public ResponseEntity<List<ImagenPerfil>> findAll() {
        List<ImagenPerfil> imagenes = imagenPerfilService.findAll();
        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar imagen por ID", description = "Obtiene la información de una imagen de perfil específica por su ID.")
    public ResponseEntity<ImagenPerfil> findById(@PathVariable Long id) {
        ImagenPerfil imagen = imagenPerfilService.findById(id);
        if (imagen == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imagen);
    }

    @PostMapping
    @Operation(summary = "Subir una nueva imagen de perfil", description = "Registra una nueva imagen de perfil en el sistema.")
    public ResponseEntity<ImagenPerfil> save(@RequestBody ImagenPerfil imagenPerfil) {
        ImagenPerfil created = imagenPerfilService.save(imagenPerfil);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ImagenPerfil> upload(
        @RequestPart("file") MultipartFile file,
        @RequestParam("usuarioId") Long usuarioId) {

    try {
        ImagenPerfil imagen = imagenPerfilService.guardarImagen(file, usuarioId);
        return ResponseEntity.status(201).body(imagen);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}



    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una imagen de perfil", description = "Modifica todos los campos de una imagen de perfil existente.")
    public ResponseEntity<ImagenPerfil> update(@PathVariable Long id, @RequestBody ImagenPerfil imagenPerfil) {
        ImagenPerfil updated = imagenPerfilService.update(id, imagenPerfil);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una imagen de perfil", description = "Actualiza algunos campos de una imagen de perfil sin modificar los demás.")
    public ResponseEntity<ImagenPerfil> patch(@PathVariable Long id, @RequestBody ImagenPerfil imagenPerfil) {
        ImagenPerfil patched = imagenPerfilService.patch(id, imagenPerfil);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una imagen de perfil", description = "Elimina una imagen de perfil específica del sistema por su ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ImagenPerfil imagen = imagenPerfilService.findById(id);
        if (imagen == null) {
            return ResponseEntity.notFound().build();
        }
        imagenPerfilService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
