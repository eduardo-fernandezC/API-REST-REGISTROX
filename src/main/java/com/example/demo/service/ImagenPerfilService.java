package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.ImagenPerfil;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ImagenPerfilRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class ImagenPerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagenPerfilRepository imagenPerfilRepository;

    public List<ImagenPerfil> findAll() {
        return imagenPerfilRepository.findAll();
    }

    public ImagenPerfil findById(Long id) {
        return imagenPerfilRepository.findById(id).orElse(null);
    }

    public ImagenPerfil save(ImagenPerfil imagenPerfil) {
        return imagenPerfilRepository.save(imagenPerfil);
    }

    public void deleteById(Long id) {
        imagenPerfilRepository.deleteById(id);
    }

    public ImagenPerfil update(Long id, ImagenPerfil imagenPerfil) {
        ImagenPerfil existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setImageUrl(imagenPerfil.getImageUrl());
        existing.setUsuario(imagenPerfil.getUsuario());
        return imagenPerfilRepository.save(existing);
    }

    public ImagenPerfil guardarImagen(MultipartFile file, Long usuarioId) throws IOException {
    Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
    if (usuario == null) return null;

    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    Path path = Paths.get("uploads/" + fileName);
    Files.createDirectories(path.getParent());
    Files.write(path, file.getBytes());

    ImagenPerfil imagen = new ImagenPerfil();
    imagen.setImageUrl("https://api-rest-registrox.onrender.com/uploads/" + fileName);
    imagen.setUsuario(usuario);

    return imagenPerfilRepository.save(imagen);
    }


    public ImagenPerfil patch(Long id, ImagenPerfil imagenPerfil) {
        ImagenPerfil existing = findById(id);
        if (existing == null) {
            return null;
        }
        if (imagenPerfil.getImageUrl() != null) {
            existing.setImageUrl(imagenPerfil.getImageUrl());
        }
        if (imagenPerfil.getUsuario() != null) {
            existing.setUsuario(imagenPerfil.getUsuario());
        }
        return imagenPerfilRepository.save(existing);
    }
}
