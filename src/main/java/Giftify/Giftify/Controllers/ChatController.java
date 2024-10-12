package Giftify.Giftify.Controllers;

import Giftify.Giftify.Models.Chat;
import Giftify.Giftify.Models.Mensaje;
import Giftify.Giftify.Models.Perfil;
import Giftify.Giftify.Repositories.MensajeRepository;
import Giftify.Giftify.Repositories.PerfilRepository;
import Giftify.Giftify.Repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @PostMapping("/enviarMensaje")
    public ResponseEntity<Mensaje> enviarMensaje(
            @RequestParam Long emisorId,
            @RequestParam Long chatId,
            @RequestParam String contenido) {

        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        Optional<Perfil> emisorOpt = perfilRepository.findById(emisorId);

        if (chatOpt.isPresent() && emisorOpt.isPresent()) {
            Chat chat = chatOpt.get();
            Perfil emisor = emisorOpt.get();

            // Determinar quién es el emisor y quién es el receptor basado en el chat
            Perfil receptor;
            if (chat.getEmisor().getIdPerfil().equals(emisorId)) {
                receptor = chat.getReceptor();
            } else if (chat.getReceptor().getIdPerfil().equals(emisorId)) {
                receptor = chat.getEmisor();
            } else {
                // Si el emisor no es ni el emisor ni el receptor del chat, es un error
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Crear el mensaje con el emisor y receptor determinados
            Mensaje mensaje = new Mensaje(emisor, receptor, chat, contenido);
            mensajeRepository.save(mensaje);

            return new ResponseEntity<>(mensaje, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/historial")
    public ResponseEntity<List<Mensaje>> obtenerHistorial(@RequestParam Long chatId) {
        Optional<Chat> chatOpt = chatRepository.findById(chatId);

        if (chatOpt.isPresent()) {
            Chat chat = chatOpt.get();
            List<Mensaje> mensajes = mensajeRepository.findByChat(chat);
            return new ResponseEntity<>(mensajes, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @GetMapping("/conversaciones/{perfilId}")
    public ResponseEntity<?> obtenerConversaciones(@PathVariable Long perfilId) {
        Optional<Perfil> perfilOpt = perfilRepository.findById(perfilId);
        if (!perfilOpt.isPresent()) {
            return new ResponseEntity<>("Perfil no encontrado", HttpStatus.NOT_FOUND);
        }

        Perfil perfil = perfilOpt.get();
        // Obtener los chats donde el perfil es emisor o receptor
        List<Chat> chats = chatRepository.findChatsByPerfil(perfil);

        // Mapeamos los chats para devolver el chatId y los datos del otro perfil
        List<Map<String, Object>> conversaciones = chats.stream().map(chat -> {
            Perfil otroPerfil = chat.getEmisor().equals(perfil) ? chat.getReceptor() : chat.getEmisor();
            Map<String, Object> conversacion = new HashMap<>();
            conversacion.put("chatId", chat.getIdChat());
            conversacion.put("otroPerfil", otroPerfil);
            return conversacion;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(conversaciones, HttpStatus.OK);
    }


    @PostMapping("/iniciar")
    public synchronized ResponseEntity<?> iniciarChat(@RequestParam Long emisorId, @RequestParam Long receptorId) {
        Optional<Perfil> emisorOpt = perfilRepository.findById(emisorId);
        Optional<Perfil> receptorOpt = perfilRepository.findById(receptorId);

        if (emisorOpt.isPresent() && receptorOpt.isPresent()) {
            Perfil emisor = emisorOpt.get();
            Perfil receptor = receptorOpt.get();

            // Sincronizamos este bloque de código para evitar la condición de carrera
            synchronized (this) {
                // Verificar si ya existe un chat entre los dos usuarios
                Optional<Chat> chatOpt = chatRepository.findChatBetween(emisor, receptor);
                Chat chat;

                if (chatOpt.isPresent()) {
                    chat = chatOpt.get();  // Usar el chat existente si ya existe
                } else {
                    // Si no existe un chat, creamos uno nuevo
                    chat = new Chat(emisor, receptor);
                    chatRepository.save(chat);
                }

                // Devolver el chatId para que el frontend redirija al chat privado
                return new ResponseEntity<>(chat.getIdChat(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Emisor o receptor no encontrado", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/detalleChat")
    public ResponseEntity<Chat> obtenerDetallesChat(@RequestParam Long chatId) {
        Optional<Chat> chatOpt = chatRepository.findById(chatId);

        if (chatOpt.isPresent()) {
            return new ResponseEntity<>(chatOpt.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

