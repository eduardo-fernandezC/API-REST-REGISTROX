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

        if (imagenParcial.getImageUrl() != null) {
            existente.setImageUrl(imagenParcial.getImageUrl());
        }
        if (imagenParcial.getUsuario() != null) {
            existente.setUsuario(imagenParcial.getUsuario());
        }

        return imagenPerfilRepository.save(existente);
    }

    public ImagenPerfil guardarImagen(MultipartFile file, Long usuarioId) throws IOException {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            System.out.println("ERROR: Usuario con ID " + usuarioId + " no existe.");
            return null;
        }

        try {
            // SUBIR A CLOUDINARY USANDO EL PRESET
            var resultado = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "upload_preset", "registrox_preset"
                    )
            );

            String url = resultado.get("secure_url").toString();
            System.out.println("✔ Imagen subida correctamente a Cloudinary: " + url);

            // GUARDAR EN BD
            ImagenPerfil imagen = new ImagenPerfil();
            imagen.setImageUrl(url);
            imagen.setUsuario(usuario);

            return imagenPerfilRepository.save(imagen);

        } catch (Exception e) {
            System.out.println("ERROR Cloudinary:");
            e.printStackTrace();
            return null;
        }
    }
}
