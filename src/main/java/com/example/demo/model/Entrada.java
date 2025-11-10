package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String lugar;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false, length = 20)
    private String estado = "disponible";

    @Column(name = "codigo_qr", length = 100)
    private String codigoQR;

    @Column(nullable = false)
    private int cantidad = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // ❌ Evitar bucle infinito desde Entrada hacia CompraEntrada
    @OneToMany(mappedBy = "entrada", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CompraEntrada> compraEntradas;
}
