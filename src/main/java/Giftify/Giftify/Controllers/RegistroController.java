package Giftify.Giftify.Controllers;

import Giftify.Giftify.DTOandServices.EnvioMailService;
import Giftify.Giftify.DTOandServices.UsuarioDTO;
import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Models.Usuario;
import Giftify.Giftify.Repositories.PerfilRepository;
import Giftify.Giftify.Repositories.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private EnvioMailService envioMailService;

    @PostMapping("/register")
public ResponseEntity<?> register(@ModelAttribute RegistroRequest registroRequest,
                                  @RequestParam(value = "fotoPerfil", required = false) MultipartFile fotoPerfil) {

    System.out.println("Registro request: " + registroRequest); // Log de depuración

    // Verificar si el correo ya está en uso
    if (usuarioRepository.findByMail(registroRequest.getMail()) != null) {
        return new ResponseEntity<>("El correo ya está registrado", HttpStatus.BAD_REQUEST);
    }

    // Verificar si las contraseñas coinciden
    if (!registroRequest.getPassword().equals(registroRequest.getRepetirPassword())) {
      return new ResponseEntity<>(new ApiResponse(false, "El correo ya está registrado"), HttpStatus.BAD_REQUEST);


    }

    // Validar fecha de nacimiento (mayor de 13 años)
    LocalDate fechaNacimiento;
    try {
        fechaNacimiento = LocalDate.parse(registroRequest.getFechaNacimiento());
        if (Period.between(fechaNacimiento, LocalDate.now()).getYears() < 13) {
            return new ResponseEntity<>("El usuario debe ser mayor de 13 años", HttpStatus.BAD_REQUEST);
        }
    } catch (DateTimeParseException e) {
        return new ResponseEntity<>("Formato de fecha de nacimiento incorrecto", HttpStatus.BAD_REQUEST);
    }

    // Validar la contraseña
    if (!validarPassword(registroRequest.getPassword())) {
        return new ResponseEntity<>("La contraseña debe tener entre 8 y 16 caracteres, incluir al menos una mayúscula, un número y un carácter especial.", HttpStatus.BAD_REQUEST);
    }

    // Guardar la foto de perfil si se ha subido una
    String fotoPerfilUrl = "http://localhost:8082/static/perfiles/sinfoto.png"; // URL por defecto
    if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
        try {
            fotoPerfilUrl = guardarFotoPerfil(fotoPerfil);
        } catch (IOException e) {
            e.printStackTrace(); // Log error
            return new ResponseEntity<>("Error al guardar la foto de perfil", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear y guardar el usuario
    String contraHash = HashCodeSha1.SHA256(registroRequest.getPassword());
    Usuario usuario = new Usuario(registroRequest.getMail(), contraHash);

    System.out.println("Usuario antes de guardar: " + usuario); // Log de depuración

    // Crear y guardar el perfil
    Perfil perfil = new Perfil(registroRequest.getNombre(), registroRequest.getApellido(), fechaNacimiento, usuario, fotoPerfilUrl);

    // Establecer la relación bidireccional
    usuario.setPerfil(perfil);

    try {
        usuarioRepository.save(usuario);
    } catch (Exception e) {
        e.printStackTrace(); // Log error
        return new ResponseEntity<>("Error al guardar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Enviar correo de confirmación
    try {
        envioMailService.enviarCorreoRegistro(registroRequest.getNombre(), registroRequest.getMail());
    } catch (MessagingException e) {
        e.printStackTrace(); // Log error
        return new ResponseEntity<>("Error al enviar el correo de confirmación", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Crear respuesta exitosa
    UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getMail()); // Usar DTO para la respuesta
    return ResponseEntity.ok().body(new ApiResponse(true, "Registro exitoso", usuarioDTO));
}

    private boolean validarPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
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

    // Clase interna para representar la solicitud de registro
    public static class RegistroRequest {
        private String mail;
        private String password;
        private String repetirPassword;
        private String nombre;
        private String apellido;
        private String fechaNacimiento;

        // Getters y Setters

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

        public String getRepetirPassword() {
            return repetirPassword;
        }

        public void setRepetirPassword(String repetirPassword) {
            this.repetirPassword = repetirPassword;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getFechaNacimiento() {
            return fechaNacimiento;
        }

        public void setFechaNacimiento(String fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
        }

        @Override
        public String toString() {
            return "RegistroRequest{" +
                    "mail='" + mail + '\'' +
                    ", password='" + password + '\'' +
                    ", repetirPassword='" + repetirPassword + '\'' +
                    ", nombre='" + nombre + '\'' +
                    ", apellido='" + apellido + '\'' +
                    ", fechaNacimiento='" + fechaNacimiento + '\'' +
                    '}';
        }
    }

   
}
