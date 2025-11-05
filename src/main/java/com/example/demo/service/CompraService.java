package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Compra;
import com.example.demo.repository.CompraRepository;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    public List<Compra> findAll() {
        return compraRepository.findAll();
    }

    public Compra findById(Long id) {
        return compraRepository.findById(id).orElse(null);
    }

    public Compra save(Compra compra) {
        return compraRepository.save(compra);
    }

    public void deleteById(Long id) {
        compraRepository.deleteById(id);
    }

    public Compra update(Long id, Compra compra) {
        Compra existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setUsuario(compra.getUsuario());
        existing.setEntradas(compra.getEntradas());
        existing.setFechaCompra(compra.getFechaCompra());
        return compraRepository.save(existing);
    }

    public Compra patch(Long id, Compra compra) {
        Compra existing = findById(id);
        if (existing == null) {
            return null;
        }
        if (compra.getUsuario() != null) {
            existing.setUsuario(compra.getUsuario());
        }
        if (compra.getEntradas() != null) {
            existing.setEntradas(compra.getEntradas());
        }
        if (compra.getFechaCompra() != null) {
            existing.setFechaCompra(compra.getFechaCompra());
        }
        return compraRepository.save(existing);
    }
}
