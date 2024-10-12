package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.Chat;
import Giftify.Giftify.Models.Mensaje;
import Giftify.Giftify.Models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // Este método busca los mensajes entre un emisor y un receptor basado en los IDs de sus perfiles
    List<Mensaje> findByEmisorIdPerfilAndReceptorIdPerfil(Long emisorId, Long receptorId);

    // Método personalizado para encontrar conversaciones basadas en los perfiles
    @Query("SELECT DISTINCT CASE WHEN m.emisor = :perfil THEN m.receptor ELSE m.emisor END " +
            "FROM Mensaje m WHERE m.emisor = :perfil OR m.receptor = :perfil")
    List<Perfil> findConversaciones(@Param("perfil") Perfil perfil);
    List<Mensaje> findByChat(Chat chat);
    @Query("SELECT m FROM Mensaje m WHERE m.chat.idChat = :chatId")
    List<Mensaje> findByChatId(@Param("chatId") Long chatId);
}

