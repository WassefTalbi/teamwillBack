package Esprit.tn.EspritJobGetaway.Repository;

import Esprit.tn.EspritJobGetaway.Entity.Application;
import Esprit.tn.EspritJobGetaway.Entity.Entretien;
import Esprit.tn.EspritJobGetaway.Enum.TypeEntretien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {

    boolean existsByApplicationAndTypeEntretien(Application app, TypeEntretien type);
    List<Entretien> findByApplication(Application app);
}