package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.model.ImagenPerfil;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ImagenPerfilRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class ImagenPerfilService {

    @Autowired
    private Cloudinary cloudinary;

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

    var result = cloudinary.uploader().upload(file.getBytes(),
            ObjectUtils.asMap("folder", "registrox/perfiles"));

    String url = (String) result.get("secure_url");

    ImagenPerfil imagen = new ImagenPerfil();
    imagen.setImageUrl(url);
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
