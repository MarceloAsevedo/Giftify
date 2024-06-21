package Giftify.Giftify.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPerfil;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private int usuarioId;

    public Perfil() {}

    public Perfil(String nombre, String apellido, LocalDate fechaNacimiento, int usuarioId) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.usuarioId = usuarioId;
    }
}