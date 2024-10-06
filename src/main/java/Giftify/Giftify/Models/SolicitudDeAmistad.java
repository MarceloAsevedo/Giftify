package Giftify.Giftify.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud_amistad")
public class SolicitudDeAmistad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emisor_id", nullable = false)
    private Perfil emisor;

    @ManyToOne
    @JoinColumn(name = "receptor_id", nullable = false)
    private Perfil receptor;

    @Column(name = "status", nullable = false)
    private String status; // "PENDING", "ACCEPTED", "REJECTED"

    @Column(name = "fecha_hora_envio", nullable = false)
    private LocalDateTime fechaHoraEnvio;

    // Constructors, Getters, and Setters

    public SolicitudDeAmistad() {}

    public SolicitudDeAmistad(Perfil emisor, Perfil receptor, String status) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.status = status;
        this.fechaHoraEnvio = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Perfil getEmisor() { return emisor; }
    public Perfil getReceptor() { return receptor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getFechaHoraEnvio() { return fechaHoraEnvio; }
}
