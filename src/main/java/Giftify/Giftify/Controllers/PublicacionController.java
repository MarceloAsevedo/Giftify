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
import java.util.Comparator;
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
    public ResponseEntity<?> crearPublicacion(
            @RequestParam("descripcionPublicacion") String descripcionPublicacion,
            @RequestParam(value = "fotoPublicacion", required = false) MultipartFile fotoPublicacion,
            @RequestParam("idPerfil") Long idPerfil,
            @RequestParam(value = "privado", defaultValue = "true") boolean privado // Agregar campo de privacidad
    ) {
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

        // Establecer si la publicación es privada o pública
        publicacion.setPrivado(privado); // Añadir el valor de esPrivada al campo privado

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

    // Obtener todas las publicaciones, ordenadas de más reciente a más antigua
    @GetMapping("/todas")
    public ResponseEntity<?> obtenerPublicaciones() {
        List<PublicacionDTO> publicaciones = publicacionRepository.findAll().stream()
                .sorted(Comparator.comparing(Publicacion::getFechaHoraPublicado).reversed())  // Orden descendente
                .map(publicacion -> new PublicacionDTO(
                        publicacion.getDescripcionPublicacion(),
                        publicacion.getFechaHoraPublicado(),
                        publicacion.getFotoPublicacion(),
                        publicacion.getPerfil().getNombre(),
                        publicacion.getPerfil().getApellido(),
                        publicacion.getPerfil().getFotoPerfil()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(publicaciones, HttpStatus.OK);
    }

    @GetMapping("/{perfilId}/usuario/{usuarioId}/publicaciones")
    public ResponseEntity<?> obtenerPublicacionesConPrivacidad(@PathVariable Long perfilId, @PathVariable Long usuarioId) {
        Optional<Perfil> optionalPerfil = perfilRepository.findById(perfilId);
        Optional<Perfil> optionalUsuario = perfilRepository.findById(usuarioId);

        if (!optionalPerfil.isPresent() || !optionalUsuario.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado", HttpStatus.NOT_FOUND);
        }

        Perfil perfil = optionalPerfil.get();
        Perfil usuario = optionalUsuario.get();
        boolean esAmigo = perfil.getAmigos().stream().anyMatch(amigo -> amigo.getIdPerfil().equals(usuario.getIdPerfil()));

        if (!perfil.isEsprivada() || esAmigo) {  // Mostrar si el perfil es público o si es amigo
            List<PublicacionDTO> publicaciones = publicacionRepository.findAll().stream()
                    .filter(publicacion -> publicacion.getPerfil().getIdPerfil().equals(perfilId)
                            && (!publicacion.isPrivado() || esAmigo)) // Filtro de publicaciones privadas
                    .sorted(Comparator.comparing(Publicacion::getFechaHoraPublicado).reversed())  // Orden descendente
                    .map(publicacion -> new PublicacionDTO(
                            publicacion.getDescripcionPublicacion(),
                            publicacion.getFechaHoraPublicado(),
                            publicacion.getFotoPublicacion(),
                            publicacion.getPerfil().getNombre(),
                            publicacion.getPerfil().getApellido(),
                            publicacion.getPerfil().getFotoPerfil()
                    ))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(publicaciones, HttpStatus.OK);
        }

        return new ResponseEntity<>("El perfil es privado y no son amigos", HttpStatus.FORBIDDEN);
    }


    // Obtener solo las publicaciones del perfil (página de perfil propio)
    @GetMapping("/{perfilId}/publicaciones")
    public ResponseEntity<?> obtenerPublicacionesPorPerfil(@PathVariable Long perfilId) {
        List<PublicacionDTO> publicaciones = publicacionRepository.findAll().stream()
                .filter(publicacion -> publicacion.getPerfil().getIdPerfil().equals(perfilId))
                .sorted(Comparator.comparing(Publicacion::getFechaHoraPublicado).reversed())  // Orden descendente
                .map(publicacion -> new PublicacionDTO(
                        publicacion.getDescripcionPublicacion(),
                        publicacion.getFechaHoraPublicado(),
                        publicacion.getFotoPublicacion(),
                        publicacion.getPerfil().getNombre(),
                        publicacion.getPerfil().getApellido(),
                        publicacion.getPerfil().getFotoPerfil()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(publicaciones, HttpStatus.OK);
    }

    // Obtener publicaciones para la página de inicio (de amigos o perfiles públicos)
    @GetMapping("/inicio/{usuarioId}")
    public ResponseEntity<?> obtenerPublicacionesParaInicio(@PathVariable Long usuarioId) {
        Optional<Perfil> optionalUsuario = perfilRepository.findById(usuarioId);
        if (!optionalUsuario.isPresent()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        Perfil usuario = optionalUsuario.get();

        // Obtener todas las publicaciones, incluyendo las del propio usuario logueado
        List<PublicacionDTO> publicaciones = publicacionRepository.findAll().stream()
                .filter(publicacion -> {
                    Perfil autor = publicacion.getPerfil();
                    boolean esAmigo = usuario.getAmigos().contains(autor);
                    // Mostrar si es amigo, perfil público, o si la publicación es del usuario logueado
                    return (esAmigo || !autor.isEsprivada() || autor.getIdPerfil().equals(usuario.getIdPerfil())) &&
                            (!publicacion.isPrivado() || esAmigo || autor.getIdPerfil().equals(usuario.getIdPerfil()));  // Filtrar publicaciones privadas
                })
                .sorted(Comparator.comparing(Publicacion::getFechaHoraPublicado).reversed())  // Orden descendente
                .map(publicacion -> new PublicacionDTO(
                        publicacion.getDescripcionPublicacion(),
                        publicacion.getFechaHoraPublicado(),
                        publicacion.getFotoPublicacion(),
                        publicacion.getPerfil().getNombre(),
                        publicacion.getPerfil().getApellido(),
                        publicacion.getPerfil().getFotoPerfil()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(publicaciones, HttpStatus.OK);
    }
//HOLIS
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

