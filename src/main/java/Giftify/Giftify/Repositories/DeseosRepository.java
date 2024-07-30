package Giftify.Giftify.Repositories;

import Giftify.Giftify.Models.Deseo;
import Giftify.Giftify.Models.ListaDeseo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeseosRepository extends JpaRepository<Deseo, Long> {
    List<Deseo> findByListaDeseo(ListaDeseo listaDeseo);
}
