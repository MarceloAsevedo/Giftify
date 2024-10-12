package Giftify.Giftify.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat")
    private Long idChat;

    @ManyToOne
    @JoinColumn(name = "emisor_id", referencedColumnName = "id_perfil")
    private Perfil emisor;

    @ManyToOne
    @JoinColumn(name = "receptor_id", referencedColumnName = "id_perfil")
    private Perfil receptor;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Mensaje> mensajes;

    public Chat() {
    }

    public Chat(Perfil emisor, Perfil receptor) {
        this.emisor = emisor;
        this.receptor = receptor;
    }

    // Getters y setters

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
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

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
