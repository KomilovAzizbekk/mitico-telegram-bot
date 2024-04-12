package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Role;
import uz.mediasolutions.miticodeliverytelegrambot.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName roleName);

}
