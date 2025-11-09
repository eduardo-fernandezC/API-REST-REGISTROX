package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Relación con usuario (ya existente)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // NUEVO: Campo virtual (no existe en la BD, solo para devolver el email)
    @JsonProperty("usuarioEmail")
    public String getUsuarioEmail() {
        // Si el usuario no es nulo, devuelve su email; si no, null
        return usuario != null ? usuario.getEmail() : null;
    }
}
