package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Step;
import uz.mediasolutions.miticodeliverytelegrambot.enums.StepName;

public interface StepRepository extends JpaRepository<Step, Long> {

    Step findByName(StepName stepName);

}
