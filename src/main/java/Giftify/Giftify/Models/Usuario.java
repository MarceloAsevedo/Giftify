package Giftify.Giftify.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;
    private String mail;
    private String password;

    // Getters and setters
    public int getId() {
        return idUsuario;
    }

    public void setId(int id) {
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

    public Usuario(int id, String mail, String password) {
        idUsuario = id;
        this.mail = mail;
        this.password = password;
    }

    public Usuario() {}

    public Usuario(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }
}