package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.MeasureUnit;

public interface MeasureUnitRepository extends JpaRepository<MeasureUnit, Long> {

    Page<MeasureUnit> findAllByNameRuContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrderByCreatedAtDesc(String nameUz, String nameRu, Pageable pageable);

    Page<MeasureUnit> findAllByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);
}
