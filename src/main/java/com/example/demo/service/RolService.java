package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Rol findById(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    public void deleteById(Long id) {
        rolRepository.deleteById(id);
    }

    public Rol update(Long id, Rol rol) {
        Rol existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setNombre(rol.getNombre());
        return rolRepository.save(existing);
    }

    public Rol patch(Long id, Rol rol) {
        Rol existing = findById(id);
        if (existing == null) {
            return null;
        }
        if (rol.getNombre() != null) {
            existing.setNombre(rol.getNombre());
        }
        return rolRepository.save(existing);
    }
}
