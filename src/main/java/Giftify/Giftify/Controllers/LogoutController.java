package Giftify.Giftify.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/usuarios")
public class LogoutController {

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidar la sesión
        request.getSession().invalidate();

        // Enviar respuesta de éxito en JSON
        response.setStatus(HttpServletResponse.SC_OK);
    }
}