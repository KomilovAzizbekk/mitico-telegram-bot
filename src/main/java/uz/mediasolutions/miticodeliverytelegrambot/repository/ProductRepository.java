package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Category;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByOrderByNumberAsc(Pageable pageable);

    Page<Product> findAllByActiveIsTrueOrderByNumberAsc(Pageable pageable);

    Page<Product> findAllByDescriptionRuContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrCategoryNameRuContainsIgnoreCaseOrCategoryNameUzContainsIgnoreCaseOrderByNumberAsc(String dRu, String dUz, String nameUz, String nameRu, String catNameRu, String catNameUz, Pageable pageable);

    Page<Product> findAllByActiveIsTrueAndDescriptionRuContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrCategoryNameRuContainsIgnoreCaseOrCategoryNameUzContainsIgnoreCaseOrderByNumberAsc(String dRu, String dUz, String nameUz, String nameRu, String catNameRu, String catNameUz, Pageable pageable);

    boolean existsByNumberAndCategoryId(Long number, Long categoryId);

    boolean existsByNumberAndId(Long number, Long id);

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    @Query(value = "SELECT DISTINCT p.* FROM products p\n" +
            "       INNER JOIN variations v on p.id = v.product_id\n" +
            "       INNER JOIN categories c on c.id = p.category_id\n" +
            "       WHERE c.active = true\n" +
            "       AND v.active = true\n" +
            "       AND p.active = true\n" +
            "       AND p.category_id = :categoryId\n" +
            "       AND v is not null\n" +
            "       AND v.deleted = false\n" +
            "       AND p.deleted = false\n" +
            "       ORDER BY p.number", nativeQuery = true)
    List<Product> findAllByCategoryIdAndVariationsIsNotEmptyAndActiveIsTrueAndCategoryActiveIsTrueAndVariationsActiveIsTrueOrderByNumberAsc(@Param("categoryId") Long categoryId);

    List<Product> findAllByCategoryId(Long id);

    @Query(value = "select * from products p where p.id=:id", nativeQuery = true)
    Product getProduct(Long id);

}
