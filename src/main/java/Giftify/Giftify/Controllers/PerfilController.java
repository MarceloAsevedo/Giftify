package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Repositories.PerfilRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;

    @GetMapping("/verperfil/{id}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long id) {
        Optional<Perfil> optionalPerfil = perfilRepository.findById(id);
        if (!optionalPerfil.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalPerfil.get(), HttpStatus.OK);
    }

    @PostMapping("/editarperfil/{id}")
    public ResponseEntity<?> editarPerfil(@PathVariable Long id,
                                          @RequestParam("nombre") String nombre,
                                          @RequestParam("apellido") String apellido,
                                          @RequestParam("fechaNacimiento") String fechaNacimiento,
                                          @RequestParam("link") String link,
                                          @RequestParam("descripcion") String descripcion,
                                          @RequestParam(value = "fotoPerfil", required = false) MultipartFile fotoPerfil) {
        // Validar edad (mayor de 13 años)
        LocalDate fechaNac;
        try {
            fechaNac = LocalDate.parse(fechaNacimiento);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Formato de fecha de nacimiento incorrecto", HttpStatus.BAD_REQUEST);
        }

        if (Period.between(fechaNac, LocalDate.now()).getYears() < 13) {
            return new ResponseEntity<>("El usuario debe ser mayor de 13 años", HttpStatus.BAD_REQUEST);
        }

        // Buscar el perfil existente por ID
        Optional<Perfil> optionalPerfilExistente = perfilRepository.findById(id);
        if (!optionalPerfilExistente.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
        }

        Perfil perfilExistente = optionalPerfilExistente.get();

        // Actualizar los datos del perfil
        perfilExistente.setNombre(nombre);
        perfilExistente.setApellido(apellido);
        perfilExistente.setFechaNacimiento(fechaNac);
        perfilExistente.setLink(link);
        perfilExistente.setDescripcion(descripcion);

        // Manejar la foto de perfil
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            try {
                String fotoPerfilUrl = guardarFotoPerfil(fotoPerfil);
                perfilExistente.setFotoPerfil(fotoPerfilUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error al guardar la foto de perfil", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            perfilExistente.setFotoPerfil("http://localhost:8082/static/perfiles/sinfoto.png");
        }

        // Guardar el perfil actualizado
        perfilRepository.save(perfilExistente);

        return new ResponseEntity<>(perfilExistente, HttpStatus.OK);
    }
    //AMIGOS
    @PostMapping("/{id}/amigos")
    public ResponseEntity<?> agregarAmigo(@PathVariable Long id, @RequestParam Long amigoId) {
        Optional<Perfil> perfilOpt = perfilRepository.findById(id);
        Optional<Perfil> amigoOpt = perfilRepository.findById(amigoId);

        if (perfilOpt.isPresent() && amigoOpt.isPresent()) {
            Perfil perfil = perfilOpt.get();
            Perfil amigo = amigoOpt.get();

            perfil.getAmigos().add(amigo);
            amigo.getAmigos().add(perfil);

            perfilRepository.save(perfil);
            perfilRepository.save(amigo);

            return new ResponseEntity<>(perfil, HttpStatus.OK);
        }

        return new ResponseEntity<>("Perfil o amigo no encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPerfiles(@RequestParam String query, @RequestParam Long perfilId) {
        List<Perfil> perfiles = perfilRepository.buscarPerfiles(query, perfilId);
        return new ResponseEntity<>(perfiles, HttpStatus.OK);
    }

    @GetMapping("/{id}/amigos")
    public ResponseEntity<?> obtenerAmigos(@PathVariable Long id) {
        Optional<Perfil> optionalPerfil = perfilRepository.findById(id);
        if (!optionalPerfil.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
        }
        Perfil perfil = optionalPerfil.get();
        Set<Perfil> amigos = perfil.getAmigos();
        return new ResponseEntity<>(amigos, HttpStatus.OK);
    }
@GetMapping("/{id}/esamigo/{amigoId}")
public ResponseEntity<?> esAmigo(@PathVariable Long id, @PathVariable Long amigoId) {
    Optional<Perfil> perfilOpt = perfilRepository.findById(id);
    Optional<Perfil> amigoOpt = perfilRepository.findById(amigoId);

    if (perfilOpt.isPresent() && amigoOpt.isPresent()) {
        Perfil perfil = perfilOpt.get();
        Perfil amigo = amigoOpt.get();

        boolean sonAmigos = perfil.getAmigos().contains(amigo);

        return new ResponseEntity<>(sonAmigos, HttpStatus.OK);
    }

    return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
}

    private String guardarFotoPerfil(MultipartFile fotoPerfil) throws IOException {
        String directory = "static/perfiles/";
        String filename = fotoPerfil.getOriginalFilename();
        Path filepath = Paths.get(directory, filename);
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, fotoPerfil.getBytes());

        // Construir la URL completa
        String baseUrl = "http://localhost:8082";
        return baseUrl + "/" + directory + filename;
    }

    @Getter
    @Setter
    public static class PerfilRequest {
        private String nombre;
        private String apellido;
        private String fechaNacimiento;
        private String fotoPerfil; 
        private String link;
        private String descripcion;
    }
}
