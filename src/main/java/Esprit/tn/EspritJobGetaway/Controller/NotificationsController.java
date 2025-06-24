package Esprit.tn.EspritJobGetaway.Controller;

import Esprit.tn.EspritJobGetaway.Entity.Notifications;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Service.NotificationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationsController {

    private  NotificationsService notificationsService;




    @GetMapping("/user/{receiveuserId}")
    public ResponseEntity<List<Notifications>> getNotificationsByReceiveuserId(@PathVariable Long receiveuserId) {
        List<Notifications> notifications = notificationsService.getNotificationsByReceiveuserId(receiveuserId);
        return ResponseEntity.ok(notifications);
    }

        @PostMapping("/createNotification")
    public ResponseEntity<?> createNotification(
        @RequestParam("message") String message,
        @RequestParam("senduserId") Long senduserId,
        @RequestParam("offerId") Long offerId,
        @RequestParam("receiveuserId") Long receiveuserId) {
    try {
        Notifications createdNotification = notificationsService.createNotification(message, senduserId, offerId, receiveuserId);
        return ResponseEntity.ok(createdNotification);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
    }
}

    @GetMapping("/{id}")
    public ResponseEntity<Notifications> getNotificationById(@PathVariable Long id) {
        Notifications notification = notificationsService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationsService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unreadCount/{receiveuserId}")
    public ResponseEntity<Long> countUnreadNotificationsByReceiveuserId(@PathVariable Long receiveuserId) {
        long count = notificationsService.countUnreadNotificationsByReceiveuserId(receiveuserId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/markAsRead/{id}")
    public ResponseEntity<Notifications> markNotificationAsRead(@PathVariable Long id) {
        try {
            Notifications updatedNotification = notificationsService.markNotificationAsRead(id);
            return ResponseEntity.ok(updatedNotification);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

        @GetMapping("/senduser/{senduserId}")
    public ResponseEntity<User> getUserBySenduserId(@PathVariable Long senduserId) {
        User user = notificationsService.getUserBySenduserId(senduserId);
        return ResponseEntity.ok(user);
    }
}