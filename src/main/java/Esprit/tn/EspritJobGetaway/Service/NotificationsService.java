package Esprit.tn.EspritJobGetaway.Service;

import Esprit.tn.EspritJobGetaway.Entity.Notifications;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Repository.NotificationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationsService {

    private NotificationsRepository notificationsRepository;
    private final UserService userService;




    public List<Notifications> getNotificationsByReceiveuserId(Long receiveuserId) {
        return notificationsRepository.findByReceiveuserId(receiveuserId);
    }

    public Notifications createNotification(String message, Long senduserId, Long offerId, Long receiveuserId) {
    Notifications newNotification = new Notifications();
    newNotification.setMessage(message);
    newNotification.setSenduserId(senduserId);
    newNotification.setOfferId(offerId);
    newNotification.setReceiveuserId(receiveuserId);
    newNotification.setRead(false); // Set read to false by default
    // The createdAt, createdAtDate, and createdAtTime fields are set automatically by @PrePersist
    return notificationsRepository.save(newNotification);
}

    public Notifications getNotificationById(Long id) {
        Optional<Notifications> notification = notificationsRepository.findById(id);
        if (!notification.isPresent()) {
            throw new RuntimeException("Notification not found with id " + id);
        }
        return notification.get();
    }

    public void deleteNotification(Long id) {
        notificationsRepository.deleteById(id);
    }

    public long countUnreadNotificationsByReceiveuserId(Long receiveuserId) {
        return notificationsRepository.countUnreadByReceiveuserId(receiveuserId);
    }

    public Notifications markNotificationAsRead(Long id) {
        Optional<Notifications> notification = notificationsRepository.findById(id);
        if (!notification.isPresent()) {
            throw new RuntimeException("Notification not found with id " + id);
        }
        Notifications existingNotification = notification.get();
        existingNotification.setRead(true);
        return notificationsRepository.save(existingNotification);
    }

    public User getUserBySenduserId(Long senduserId) {
        return userService.findUserById(senduserId);
    }









}