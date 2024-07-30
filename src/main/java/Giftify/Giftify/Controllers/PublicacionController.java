package Giftify.Giftify.Controllers;


import Giftify.Giftify.Models.Publicacion;
import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Repositories.PublicacionRepository;
import Giftify.Giftify.Repositories.PerfilRepository;
import Giftify.Giftify.DTOandServices.PublicacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @PostMapping("/nueva")
    public ResponseEntity<?> crearPublicacion(@RequestParam("descripcionPublicacion") String descripcionPublicacion,
                                              @RequestParam(value = "fotoPublicacion", required = false) MultipartFile fotoPublicacion,
                                              @RequestParam("idPerfil") Long idPerfil) {

        // Validar perfil existente
        Optional<Perfil> optionalPerfil = perfilRepository.findById(idPerfil);
        if (!optionalPerfil.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado con ID: " + idPerfil, HttpStatus.NOT_FOUND);
        }

        Perfil perfil = optionalPerfil.get();

        // Crear nueva publicación
        Publicacion publicacion = new Publicacion();
        publicacion.setDescripcionPublicacion(descripcionPublicacion);
        publicacion.setFechaHoraPublicado(LocalDateTime.now());
        publicacion.setPerfil(perfil);

        // Manejar la foto de la publicación
        if (fotoPublicacion != null && !fotoPublicacion.isEmpty()) {
            try {
                String fotoPublicacionUrl = guardarFotoPublicacion(fotoPublicacion);
                publicacion.setFotoPublicacion(fotoPublicacionUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error al guardar la foto de la publicación",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Guardar la publicación
        publicacionRepository.save(publicacion);

        return new ResponseEntity<>(publicacion, HttpStatus.CREATED);
    }
    
    @GetMapping("/todas")
    public ResponseEntity<?> obtenerPublicaciones() {
        List<PublicacionDTO> publicaciones = publicacionRepository.findAll().stream()
                .map(publicacion -> new PublicacionDTO(
                        publicacion.getDescripcionPublicacion(),
                        publicacion.getFechaHoraPublicado(),
                        publicacion.getFotoPublicacion(),
                        publicacion.getPerfil().getNombre(),
                        publicacion.getPerfil().getApellido(),
                        publicacion.getPerfil().getFotoPerfil()// esto deberiiiia traer el nombre del perfil, vamo a ve, si anda hacer lo mismo con el apellido tambien asi tre los 2 juntos, y la foto igual
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(publicaciones, HttpStatus.OK);
    }
    private String guardarFotoPublicacion(MultipartFile fotoPublicacion) throws IOException {
        String directory = "static/publicaciones/";
        String filename = fotoPublicacion.getOriginalFilename();
        Path filepath = Paths.get(directory, filename);
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, fotoPublicacion.getBytes());

        // Construir la URL completa para guardar la imagen
        String baseUrl = "http://localhost:8082";
        return baseUrl + "/" + directory + filename;
    }
}
