package Esprit.tn.EspritJobGetaway.Repository;

import Esprit.tn.EspritJobGetaway.Entity.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OffreRepository extends JpaRepository<Offre, Long> {

@Query("SELECT o FROM Offre o WHERE o.user.id = :userId")
List<Offre> findByUserId(Long userId);

@Query("SELECT o FROM Offre o WHERE o.user.id <> :userId")
List<Offre> findByUserIdNot(Long userId);



}