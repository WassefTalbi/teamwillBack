package Esprit.tn.EspritJobGetaway.Repository;

import Esprit.tn.EspritJobGetaway.Entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findByReceiveuserId(Long receiveuserId);
    @Query("SELECT COUNT(n) FROM Notifications n WHERE n.receiveuserId = :receiveuserId AND n.isRead = false")
    long countUnreadByReceiveuserId(@Param("receiveuserId") Long receiveuserId);

}