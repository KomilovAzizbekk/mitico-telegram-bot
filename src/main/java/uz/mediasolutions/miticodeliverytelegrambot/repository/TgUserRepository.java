package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.TgUser;

import java.util.Optional;

public interface TgUserRepository extends JpaRepository<TgUser, Long> {

    Page<TgUser> findAllByNameContainsIgnoreCaseOrUsernameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCaseOrderByCreatedAtDesc(String name, String username, String phoneNumber, Pageable pageable);

    Page<TgUser> findAllByOrderByCreatedAtDesc(Pageable pageable);

    TgUser findByChatId(String chatId);

    boolean existsByChatId(String chatId);

    Optional<TgUser> findByPhoneNumber(String username);
}
