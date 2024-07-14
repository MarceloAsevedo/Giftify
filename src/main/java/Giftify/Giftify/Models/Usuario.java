package Giftify.Giftify.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "mail", nullable = false)
    private String mail;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Perfil perfil;

    
     @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", perfil=" + (perfil != null ? perfil.getIdPerfil() : "null") +
                '}';
    }
    
   public Usuario(String mail, String contraHash) {
    this.mail = mail;
    this.password = contraHash;
}

    public Usuario() {
    }

    public Usuario(Long idUsuario, String mail, String password, Perfil perfil) {
        this.idUsuario = idUsuario;
        this.mail = mail;
        this.password = password;
        this.perfil = perfil;
    }

    // Getters and setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
