
package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Models.Usuario;
import Giftify.Giftify.Repositories.PerfilRepository;
import Giftify.Giftify.Repositories.UsuarioRepository;
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

   

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroRequest registroRequest) {
        
        String contraHassh = HashCodeSha1.SHA1(registroRequest.getPassword());

       
        Usuario usuario = new Usuario(registroRequest.getMail(), contraHassh);
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Crear un perfil asociado al usuario
        Perfil perfil = new Perfil(registroRequest.getNombre(), registroRequest.getApellido(), registroRequest.getFechaNacimiento(), savedUsuario.getId());
        perfilRepository.save(perfil);

        return new ResponseEntity<>(savedUsuario, HttpStatus.CREATED);
    }

    public static class RegistroRequest {
        private String mail;
        private String password;
        private String nombre;
        private String apellido;
        private String fechaNacimiento;

        // Getters and setters
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