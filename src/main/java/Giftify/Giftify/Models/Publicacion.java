package Giftify.Giftify.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "publicacion")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicacion")
    private Long idPublicacion;

    @Column(name = "descripcion_pub", nullable = false)
    private String descripcionPublicacion;

    @Column(name = "fechayhora_publicado", nullable = false)
    private LocalDateTime fechaHoraPublicado;

    @Column(name = "foto_publicacion", nullable = true)
    private String fotoPublicacion;

    @Column(name = "privado", nullable = false)
    private boolean privado;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    @JsonBackReference
    private Perfil perfil;

   
    public Publicacion(){}

    public Publicacion(String descripcionPublicacion, LocalDateTime fechaPublicado, String fotoPublicacion, Perfil perfil) {
        this.descripcionPublicacion = descripcionPublicacion;
        this.fechaHoraPublicado = fechaPublicado;
        this.fotoPublicacion = fotoPublicacion;
        this.perfil = perfil;
    }

    // Getters y Setters
    public Long getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Long idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getDescripcionPublicacion() {
        return descripcionPublicacion;
    }

    public void setDescripcionPublicacion(String descripcionPublicacion) {
        this.descripcionPublicacion = descripcionPublicacion;
    }

    public LocalDateTime getFechaHoraPublicado() {
        return fechaHoraPublicado;
    }

    public void setFechaHoraPublicado(LocalDateTime fechaHoraPublicado) {
        this.fechaHoraPublicado = fechaHoraPublicado;
    }

    public String getFotoPublicacion() {
        return fotoPublicacion;
    }

    public void setFotoPublicacion(String fotoPublicacion) {
        this.fotoPublicacion = fotoPublicacion;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public boolean isPrivado() {
        return privado;
    }

    public void setPrivado(boolean privado) {
        this.privado = privado;
    }

}
