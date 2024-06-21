package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Repositories.PerfilRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios") // Se encarga de gestionar el controlador
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;

    @PutMapping("/editarperfil/{id}")
    public ResponseEntity<?> editarperfil(@PathVariable Long id, @RequestBody PerfilRequest perfilRequest) {

        // Validar edad (mayor de 13 años)
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(perfilRequest.getFechaNacimiento());
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Formato de fecha de nacimiento incorrecto", HttpStatus.BAD_REQUEST);
        }

        if (Period.between(fechaNacimiento, LocalDate.now()).getYears() < 13) {
            return new ResponseEntity<>("El usuario debe ser mayor de 13 años", HttpStatus.BAD_REQUEST);
        }

        // Buscar el perfil existente por ID
        Optional<Perfil> optionalPerfilExistente = perfilRepository.findById(id);
        if (!optionalPerfilExistente.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
        }

        Perfil perfilExistente = optionalPerfilExistente.get();

        // Actualizar los datos del perfil
        perfilExistente.setNombre(perfilRequest.getNombre());
        perfilExistente.setApellido(perfilRequest.getApellido());
        perfilExistente.setFechaNacimiento(fechaNacimiento);

        // Guardar el perfil actualizado
        perfilRepository.save(perfilExistente);

        return new ResponseEntity<>(perfilExistente, HttpStatus.OK);
    }

    @Getter
    @Setter
    public static class PerfilRequest {
        private String nombre;
        private String apellido;
        private String fechaNacimiento; // String type
        public PerfilRequest() {
        }
    }
}
