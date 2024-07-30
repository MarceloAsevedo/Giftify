package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.Deseo;
import Giftify.Giftify.Models.ListaDeseo;
import Giftify.Giftify.Repositories.DeseosRepository;
import Giftify.Giftify.Repositories.ListaDeseoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deseos")
public class DeseoController {

    @Autowired
    private DeseosRepository deseosRepository;

    @Autowired
    private ListaDeseoRepository listaDeseoRepository;

    @PostMapping("/nuevo")
    public ResponseEntity<?> crearDeseo(@RequestParam("tituloDeseo") String tituloDeseo,
                                        @RequestParam(value = "url", required = false) String url,
                                        @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                        @RequestParam(value = "descripcion", required = false) String descripcion,
                                        @RequestParam("idLista") Long idLista) {

        Optional<ListaDeseo> optionalListaDeseo = listaDeseoRepository.findById(idLista);
        if (!optionalListaDeseo.isPresent()) {
            return new ResponseEntity<>("Lista de deseos no encontrada con ID: " + idLista, HttpStatus.NOT_FOUND);
        }

        ListaDeseo listaDeseo = optionalListaDeseo.get();
        Deseo deseo = new Deseo();
        deseo.setTituloDeseo(tituloDeseo);
        deseo.setUrl(url);
        deseo.setDescripcion(descripcion);
        deseo.setListaDeseo(listaDeseo);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String imagenUrl = guardarImagenDeseo(imagen);
                deseo.setImagen(imagenUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error al guardar la imagen del deseo", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        deseosRepository.save(deseo);
        return new ResponseEntity<>(deseo, HttpStatus.CREATED);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarDeseo(@PathVariable Long id,
                                         @RequestParam("tituloDeseo") String tituloDeseo,
                                         @RequestParam(value = "url", required = false) String url,
                                         @RequestParam(value = "descripcion", required = false) String descripcion,
                                         @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        Optional<Deseo> optionalDeseo = deseosRepository.findById(id);
        if (!optionalDeseo.isPresent()) {
            return new ResponseEntity<>("Deseo no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
        }

        Deseo deseo = optionalDeseo.get();
        deseo.setTituloDeseo(tituloDeseo);
        deseo.setUrl(url);
        deseo.setDescripcion(descripcion);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String imagenUrl = guardarImagenDeseo(imagen);
                deseo.setImagen(imagenUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error al guardar la imagen del deseo", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        deseosRepository.save(deseo);
        return new ResponseEntity<>(deseo, HttpStatus.OK);
    }

@GetMapping("/lista/{idLista}")
public ResponseEntity<?> obtenerDeseosPorLista(@PathVariable Long idLista) {
    Optional<ListaDeseo> optionalListaDeseo = listaDeseoRepository.findById(idLista);
    if (!optionalListaDeseo.isPresent()) {
        return new ResponseEntity<>("Lista de deseos no encontrada con ID: " + idLista, HttpStatus.NOT_FOUND);
    }

    ListaDeseo listaDeseo = optionalListaDeseo.get();
    List<Deseo> deseos = deseosRepository.findByListaDeseo(listaDeseo);

    return new ResponseEntity<>(deseos, HttpStatus.OK);
}

@DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarDeseo(@PathVariable Long id) {
        Optional<Deseo> optionalDeseo = deseosRepository.findById(id);
        if (!optionalDeseo.isPresent()) {
            return new ResponseEntity<>("Deseo no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
        }

        deseosRepository.deleteById(id);
        return new ResponseEntity<>("Deseo eliminado con éxito", HttpStatus.OK);
    }

    private String guardarImagenDeseo(MultipartFile imagen) throws IOException {
        String directory = "static/deseos/";
        String filename = imagen.getOriginalFilename();
        Path filepath = Paths.get(directory, filename);
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, imagen.getBytes());

        String baseUrl = "http://localhost:8082";
        return baseUrl + "/" + directory + filename;
    }
}
