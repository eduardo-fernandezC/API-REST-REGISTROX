package com.example.demo.service;

import java.io.IOException;
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

    public ImagenPerfil update(Long id, ImagenPerfil nuevaImagen) {
        ImagenPerfil existente = findById(id);
        if (existente == null) return null;

        existente.setImageUrl(nuevaImagen.getImageUrl());
        existente.setUsuario(nuevaImagen.getUsuario());

        return imagenPerfilRepository.save(existente);
    }

    public ImagenPerfil patch(Long id, ImagenPerfil imagenParcial) {
        ImagenPerfil existente = findById(id);
        if (existente == null) return null;

        if (imagenParcial.getImageUrl() != null)
            existente.setImageUrl(imagenParcial.getImageUrl());

        if (imagenParcial.getUsuario() != null)
            existente.setUsuario(imagenParcial.getUsuario());

        return imagenPerfilRepository.save(existente);
    }

    // =======================================================
    // SUBIR IMAGEN NUEVA (y reemplazar si ya existe)
    // =======================================================
    public ImagenPerfil guardarImagen(MultipartFile file, Long usuarioId) throws IOException {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return null;
        }

        // verificar si ya tiene una imagen
        ImagenPerfil existente = imagenPerfilRepository.findByUsuarioId(usuarioId);

        // si ya existe → borrar imagen anterior en Cloudinary
        if (existente != null) {
            String oldPublicId = extraerPublicId(existente.getImageUrl());
            try {
                cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());
            } catch (Exception e) {
                System.out.println("❌ Error al borrar imagen anterior en Cloudinary (no bloquea update)");
            }
        }

        // SUBIR NUEVA IMAGEN USANDO PRESET
        var resultado = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "upload_preset", "registrox_preset"
                )
        );

        String url = resultado.get("secure_url").toString();

        // si existía → actualizar
        if (existente != null) {
            existente.setImageUrl(url);
            return imagenPerfilRepository.save(existente);
        }

        // si NO existía → crear nuevo registro
        ImagenPerfil nueva = new ImagenPerfil();
        nueva.setImageUrl(url);
        nueva.setUsuario(usuario);

        return imagenPerfilRepository.save(nueva);
    }

    // =======================================================
    // EXTRAER PUBLIC_ID DESDE LA URL
    // =======================================================
    private String extraerPublicId(String url) {
        // ejemplo URL:
        // https://res.cloudinary.com/df185j75k/image/upload/v12345/registrox/perfiles/abcd1234.jpg
        String sinExtension = url.substring(0, url.lastIndexOf('.'));
        return sinExtension.substring(sinExtension.lastIndexOf('/') + 1);
    }

}
