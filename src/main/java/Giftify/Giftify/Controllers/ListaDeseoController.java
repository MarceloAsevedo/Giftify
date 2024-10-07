package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.ListaDeseo;
import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Repositories.ListaDeseoRepository;
import Giftify.Giftify.Repositories.PerfilRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/listasdeseos")
public class ListaDeseoController {

    @Autowired
    private ListaDeseoRepository listaDeseoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @PostMapping("/nueva")
    public ResponseEntity<?> crearListaDeseo(
            @RequestParam("nombreLista") String nombreLista,
            @RequestParam("idPerfil") Long idPerfil) {

        // Validación de existencia del perfil
        Optional<Perfil> optionalPerfil = perfilRepository.findById(idPerfil);
        if (!optionalPerfil.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado", HttpStatus.NOT_FOUND);
        }

        Perfil perfil = optionalPerfil.get();

        // Verificación de nombre de lista no vacío
        if (nombreLista == null || nombreLista.trim().isEmpty()) {
            return new ResponseEntity<>("El nombre de la lista no puede estar vacío", HttpStatus.BAD_REQUEST);
        }

        ListaDeseo listaDeseo = new ListaDeseo();
        listaDeseo.setNombreLista(nombreLista);
        listaDeseo.setPerfil(perfil);

        listaDeseo = listaDeseoRepository.save(listaDeseo);

        // Respuesta con éxito y detalles de la lista creada
        return new ResponseEntity<>(listaDeseo, HttpStatus.CREATED);
    }

    // Manejo de excepciones de validación y otros errores
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{perfilId}/usuario/{usuarioId}/listasdeseos")
    public ResponseEntity<?> obtenerListasDeseosConPrivacidad(@PathVariable Long perfilId, @PathVariable Long usuarioId) {
        Optional<Perfil> optionalPerfil = perfilRepository.findById(perfilId);
        Optional<Perfil> optionalUsuario = perfilRepository.findById(usuarioId);

        if (!optionalPerfil.isPresent() || !optionalUsuario.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado", HttpStatus.NOT_FOUND);
        }

        Perfil perfil = optionalPerfil.get();
        Perfil usuario = optionalUsuario.get();
        boolean esAmigo = perfil.getAmigos().stream().anyMatch(amigo -> amigo.getIdPerfil().equals(usuario.getIdPerfil()));

        // Si el perfil es público o son amigos, mostrar las listas de deseos
        if (!perfil.isEsprivada() || esAmigo) {
            List<ListaDeseo> listasDeseos = listaDeseoRepository.findByPerfil(perfil);
            return new ResponseEntity<>(listasDeseos, HttpStatus.OK);
        }

        // Si no son amigos y el perfil es privado, no mostrar las listas de deseos
        return new ResponseEntity<>("El perfil es privado y no son amigos", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/perfil/{idPerfil}")
    public ResponseEntity<?> obtenerListasPorPerfil(@PathVariable Long idPerfil) {
        Optional<Perfil> optionalPerfil = perfilRepository.findById(idPerfil);
        if (!optionalPerfil.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado con ID: " + idPerfil, HttpStatus.NOT_FOUND);
        }

        Perfil perfil = optionalPerfil.get();
        List<ListaDeseo> listasDeseos = listaDeseoRepository.findByPerfil(perfil);

        return new ResponseEntity<>(listasDeseos, HttpStatus.OK);
    }
    
  @PutMapping("/editar/{id}")
public ResponseEntity<?> editarLista(@PathVariable Long id,
                                     @RequestParam("nombreLista") String nombreLista) {

    Optional<ListaDeseo> optionalListaDeseo = listaDeseoRepository.findById(id);
    if (!optionalListaDeseo.isPresent()) {
        return new ResponseEntity<>("Lista de deseos no encontrada con ID: " + id, HttpStatus.NOT_FOUND);
    }

    ListaDeseo listaDeseo = optionalListaDeseo.get();
    listaDeseo.setNombreLista(nombreLista);

    listaDeseoRepository.save(listaDeseo);
    return new ResponseEntity<>("Lista editada con éxito", HttpStatus.OK);
}


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarLista(@PathVariable Long id) {
        Optional<ListaDeseo> optionalListaDeseo = listaDeseoRepository.findById(id);
        if (!optionalListaDeseo.isPresent()) {
            return new ResponseEntity<>("Lista de deseos no encontrada con ID: " + id, HttpStatus.NOT_FOUND);
        }

        listaDeseoRepository.deleteById(id);
        return new ResponseEntity<>("Lista de deseos eliminada con éxito", HttpStatus.OK);
    }

}
