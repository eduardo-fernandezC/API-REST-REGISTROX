package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setEmail(usuario.getEmail());
        existing.setPassword(usuario.getPassword());
        existing.setRol(usuario.getRol());
        return usuarioRepository.save(existing);
    }

    public Usuario patch(Long id, Usuario usuario) {
        Usuario existing = findById(id);
        if (existing == null) {
            return null;
        }
        if (usuario.getEmail() != null) {
            existing.setEmail(usuario.getEmail());
        }
        if (usuario.getPassword() != null) {
            existing.setPassword(usuario.getPassword());
        }
        if (usuario.getRol() != null) {
            existing.setRol(usuario.getRol());
        }
        return usuarioRepository.save(existing);
    }
}
