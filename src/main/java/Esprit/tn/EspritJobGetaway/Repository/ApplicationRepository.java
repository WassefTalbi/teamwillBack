package Esprit.tn.EspritJobGetaway.Repository;

import Esprit.tn.EspritJobGetaway.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByOfferId(Long id);
    List<Application> findByUserId(Long userId);

}