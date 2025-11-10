package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.CompraEntrada;

public interface CompraEntradaRepository extends JpaRepository<CompraEntrada, Long> {
    Optional<CompraEntrada> findByCodigoQR(String codigoQR);
}
