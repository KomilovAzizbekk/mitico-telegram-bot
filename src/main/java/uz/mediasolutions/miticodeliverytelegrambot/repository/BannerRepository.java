package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Banner;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    Page<Banner> findAllByOrderByNumberAsc(Pageable pageable);

    List<Banner> findAllByOrderByNumberAsc();

}
