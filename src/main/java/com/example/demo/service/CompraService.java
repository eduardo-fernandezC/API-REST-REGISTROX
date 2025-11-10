package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Compra;
import com.example.demo.model.CompraEntrada;
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
        // 🔹 Generar código QR único para cada CompraEntrada
        if (compra.getCompraEntradas() != null) {
            for (CompraEntrada ce : compra.getCompraEntradas()) {
                if (ce.getCodigoQR() == null || ce.getCodigoQR().isEmpty()) {
                    String qr = "QR-" + compra.getUsuario().getId() + "-" +
                                System.currentTimeMillis() + "-" +
                                ce.getEntrada().getTitulo().hashCode();
                    ce.setCodigoQR(qr);
                }

                // Establecer relación bidireccional
                ce.setCompra(compra);

                if (ce.getEstado() == null || ce.getEstado().isEmpty()) {
                    ce.setEstado("disponible");
                }
            }
        }

        return compraRepository.save(compra);
    }

    public void deleteById(Long id) {
        compraRepository.deleteById(id);
    }

    public Compra update(Long id, Compra compra) {
        Compra existing = findById(id);
        if (existing == null) return null;

        existing.setUsuario(compra.getUsuario());
        existing.setCompraEntradas(compra.getCompraEntradas());
        existing.setFechaCompra(compra.getFechaCompra());

        return compraRepository.save(existing);
    }

    public Compra patch(Long id, Compra compra) {
        Compra existing = findById(id);
        if (existing == null) return null;

        if (compra.getUsuario() != null) {
            existing.setUsuario(compra.getUsuario());
        }
        if (compra.getCompraEntradas() != null) {
            existing.setCompraEntradas(compra.getCompraEntradas());
        }
        if (compra.getFechaCompra() != null) {
            existing.setFechaCompra(compra.getFechaCompra());
        }

        return compraRepository.save(existing);
    }
}
