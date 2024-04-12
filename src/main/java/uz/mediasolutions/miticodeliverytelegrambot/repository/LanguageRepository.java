package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Language;
import uz.mediasolutions.miticodeliverytelegrambot.enums.LanguageName;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Language findByName(LanguageName languageName);
}
