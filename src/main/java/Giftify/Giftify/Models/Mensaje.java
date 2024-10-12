package Giftify.Giftify.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @ManyToOne
    @JoinColumn(name = "emisor_id", referencedColumnName = "id_perfil")
    private Perfil emisor;

    @ManyToOne
    @JoinColumn(name = "receptor_id", referencedColumnName = "id_perfil")
    private Perfil receptor;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id_chat")
    @JsonBackReference
    private Chat chat;

    @Column(name = "contenido", nullable = false)
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    public Mensaje() {
    }

    public Mensaje(Perfil emisor, Perfil receptor, Chat chat, String contenido) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.chat = chat;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }

    // Getters y setters


    public Long getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Long idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Perfil getEmisor() {
        return emisor;
    }

    public void setEmisor(Perfil emisor) {
        this.emisor = emisor;
    }

    public Perfil getReceptor() {
        return receptor;
    }

    public void setReceptor(Perfil receptor) {
        this.receptor = receptor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
