package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Variation;

import java.util.List;

public interface VariationRepository extends JpaRepository<Variation, Long> {

    Page<Variation> findAllByOrderByNumberAsc(Pageable pageable);

    Page<Variation> findAllByProductNameRuContainsIgnoreCaseOrProductNameUzContainsIgnoreCaseOrMeasureUnitNameRuContainsIgnoreCaseOrMeasureUnitNameUzContainsIgnoreCaseOrderByNumberAsc(String pNameUz, String pNameRu, String mNameUz, String mNameRu, Pageable pageable);

    boolean existsByNumber(Long number);

    boolean existsByNumberAndId(Long number, Long id);

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    List<Variation> findAllByProductIdAndActiveIsTrueOrderByNumberAsc(Long productId);

    List<Variation> findAllByMeasureUnitId(Long measureUnitId);

    @Query(value = "select * from variations v where v.id=:id", nativeQuery = true)
    Variation getVariation(Long id);

    List<Variation> findAllByProductIdAndActiveIsTrue(Long id);
}
