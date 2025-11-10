package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Compra;
import com.example.demo.model.Entrada;
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
        // Generar un código QR único para cada entrada si no tiene uno
        if (compra.getEntradas() != null) {
            for (Entrada entrada : compra.getEntradas()) {
                if (entrada.getCodigoQR() == null || entrada.getCodigoQR().isEmpty()) {
                    String qrGenerado = "QR-" + compra.getUsuario().getId() + "-" + System.currentTimeMillis() + "-" + entrada.getTitulo().hashCode();
                    entrada.setCodigoQR(qrGenerado);
                }

                // Establecer el estado inicial como "disponible" si no tiene
                if (entrada.getEstado() == null || entrada.getEstado().isEmpty()) {
                    entrada.setEstado("disponible");
                }
            }
        }

        // Guardar la compra (esto también guarda las entradas por cascada si está configurado)
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
