package Giftify.Giftify.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
//ver uso de lombok
// crear dto de usuario para esconder la password en el inicio de sesion
@Entity
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Long idUsuario;

    @Column(name="mail")
    private String mail;

    @Column(name="password")
    private String password;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Perfil perfil;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Usuario(Long idUsuario, String mail, String password, Perfil perfil) {
        this.idUsuario = idUsuario;
        this.mail = mail;
        this.password = password;
        this.perfil = perfil;
    }
    
    
    // Getters and setters
    public Long getId() {
        return idUsuario;
    }

    public void setId(Long id) {
      idUsuario = id;
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

    public Usuario(Long id, String mail, String password) {
        idUsuario = id;
        this.mail = mail;
        this.password = password;
    }

    public Usuario() {}

    public Usuario(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public Usuario getUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}