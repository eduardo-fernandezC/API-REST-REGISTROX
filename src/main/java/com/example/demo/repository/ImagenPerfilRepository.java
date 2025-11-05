package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ImagenPerfil;
import com.example.demo.model.Usuario;

@Repository
public interface ImagenPerfilRepository extends JpaRepository<ImagenPerfil, Long> {
    ImagenPerfil findByUsuario(Usuario usuario);
}
