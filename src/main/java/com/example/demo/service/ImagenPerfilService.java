package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ImagenPerfil;
import com.example.demo.repository.ImagenPerfilRepository;

@Service
public class ImagenPerfilService {

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
