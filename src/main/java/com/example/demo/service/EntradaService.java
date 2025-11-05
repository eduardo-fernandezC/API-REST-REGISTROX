package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Entrada;
import com.example.demo.repository.EntradaRepository;

@Service
public class EntradaService {

    @Autowired
    private EntradaRepository entradaRepository;

    public List<Entrada> findAll() {
        return entradaRepository.findAll();
    }

    public Entrada findById(Long id) {
        return entradaRepository.findById(id).orElse(null);
    }

    public Entrada save(Entrada entrada) {
        return entradaRepository.save(entrada);
    }

    public void deleteById(Long id) {
        entradaRepository.deleteById(id);
    }

    public Entrada update(Long id, Entrada entrada) {
        Entrada existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setTitulo(entrada.getTitulo());
        existing.setLugar(entrada.getLugar());
        existing.setPrecio(entrada.getPrecio());
        existing.setEstado(entrada.getEstado());
        existing.setCodigoQR(entrada.getCodigoQR());
        existing.setCantidad(entrada.getCantidad());
        existing.setUsuario(entrada.getUsuario());
        return entradaRepository.save(existing);
    }

    public Entrada patch(Long id, Entrada entrada) {
        Entrada existing = findById(id);
        if (existing == null) {
            return null;
        }
        if (entrada.getTitulo() != null) {
            existing.setTitulo(entrada.getTitulo());
        }
        if (entrada.getLugar() != null) {
            existing.setLugar(entrada.getLugar());
        }
        if (entrada.getPrecio() != null) {
            existing.setPrecio(entrada.getPrecio());
        }
        if (entrada.getEstado() != null) {
            existing.setEstado(entrada.getEstado());
        }
        if (entrada.getCodigoQR() != null) {
            existing.setCodigoQR(entrada.getCodigoQR());
        }
        if (entrada.getCantidad() != 0) {
            existing.setCantidad(entrada.getCantidad());
        }
        if (entrada.getUsuario() != null) {
            existing.setUsuario(entrada.getUsuario());
        }
        return entradaRepository.save(existing);
    }
}
