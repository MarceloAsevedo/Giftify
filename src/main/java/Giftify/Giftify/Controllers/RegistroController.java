
package Giftify.Giftify.Controllers;

import Giftify.Giftify.DTOandServices.EnvioMailService;
import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Models.Usuario;
import Giftify.Giftify.Repositories.PerfilRepository;
import Giftify.Giftify.Repositories.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

   
    @Autowired
    private EnvioMailService envioMailService;

      @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroRequest registroRequest) {
        registroRequest.getMail();

        if (usuarioRepository.findByMail(registroRequest.getMail()) != null) {
           
            return new ResponseEntity<>("El mail está en uso", HttpStatus.BAD_REQUEST);
        }

        if (!registroRequest.getPassword().equals(registroRequest.getRepetirPassword())) {
          
            return new ResponseEntity<>("Las contraseñas no coinciden", HttpStatus.BAD_REQUEST);
        }

        String contraHash = HashCodeSha1.SHA1(registroRequest.getPassword());

        Usuario usuario = new Usuario(registroRequest.getMail(), contraHash);
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Perfil perfil = new Perfil(registroRequest.getNombre(), registroRequest.getApellido(), registroRequest.getFechaNacimiento(), savedUsuario.getId());
        perfilRepository.save(perfil);

        try {
            envioMailService.enviarCorreoRegistro(registroRequest.getNombre(), registroRequest.getMail());
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al enviar el correo de confirmación", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Usuario registrado correctamente", HttpStatus.CREATED);
    }

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
    }
}