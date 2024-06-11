package Giftify.Giftify.DTOandServices;


import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EnvioMailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoRegistro(String nombre, String correo) throws MessagingException {
        // Configurar el mensaje de confirmación de registro
        MimeMessage messageConfirmacion = mailSender.createMimeMessage();
        messageConfirmacion.setFrom(new InternetAddress("asevedom98@gmail.com"));
        messageConfirmacion.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));
        messageConfirmacion.setSubject("Registro exitoso en Giftify");

        // Crear el cuerpo del mensaje de confirmación
        String mensajeConfirmacionTexto = "Hola " + nombre + ",\n\n" +
                                          "Gracias por registrarte en Giftify. Tu cuenta ha sido creada exitosamente.";

        MimeBodyPart bodyPartConfirmacion = new MimeBodyPart();
        bodyPartConfirmacion.setContent(mensajeConfirmacionTexto, "text/plain");

        // Agregar el cuerpo del mensaje al MimeMessage de confirmación
        Multipart multipartConfirmacion = new MimeMultipart("alternative");
        multipartConfirmacion.addBodyPart(bodyPartConfirmacion);
        messageConfirmacion.setContent(multipartConfirmacion);

        // Enviar el correo electrónico de confirmación
        mailSender.send(messageConfirmacion);
    }
}