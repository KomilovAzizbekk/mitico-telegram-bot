package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAllByOrderByNumberAsc(Pageable pageable);

    Page<Category> findAllByActiveIsTrueOrderByNumberAsc(Pageable pageable);

    List<Category> findAllByActiveIsTrueOrderByNumberAsc();

    Page<Category> findAllByActiveIsTrueAndDescriptionRuContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrderByNumberAsc(String dUz, String dRu, String nameUz, String nameRu, Pageable pageable);

    Page<Category> findAllByDescriptionRuContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrderByNumberAsc(String dUz, String dRu, String nameUz, String nameRu, Pageable pageable);

    boolean existsByNumber(Long number);

    boolean existsByNumberAndId(Long number, Long id);

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);
}
