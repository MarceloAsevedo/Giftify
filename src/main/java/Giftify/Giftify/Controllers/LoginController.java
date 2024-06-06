package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.Usuario;

import Giftify.Giftify.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/usuarios")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

   

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String contraHash = HashCodeSha1.SHA1(loginRequest.getPassword());
        Usuario usuario = usuarioRepository.findByMailAndPassword(loginRequest.getMail(), contraHash);

        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }
    }

    public static class LoginRequest {
        private String mail;
        private String password;

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
    }
}