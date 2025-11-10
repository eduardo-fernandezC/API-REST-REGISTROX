package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CompraEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrada_id")
    private Entrada entrada;

    @Column(nullable = false, length = 20)
    private String estado = "disponible";

    @Column(name = "codigo_qr", unique = true, length = 120)
    private String codigoQR;
}
