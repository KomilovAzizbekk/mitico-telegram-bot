package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Branch;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Page<Branch> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Branch findByNameUzOrNameRu(String nameUz, String nameRu);

    List<Branch> findAllByActiveIsTrue(Sort sort);

    List<Branch> findAllByActiveIsTrue();

    boolean existsByNameUzOrNameRu(String nameUz, String nameRu);

    Page<Branch> findAllByAddressRuContainsIgnoreCaseOrAddressUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrderByCreatedAtDesc(String aRu, String aUz, String nameUz, String nameRu, Pageable pageable);
}
