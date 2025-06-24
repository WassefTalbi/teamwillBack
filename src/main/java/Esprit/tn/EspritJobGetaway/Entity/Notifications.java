package Esprit.tn.EspritJobGetaway.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Long senduserId;
    private Long offerId;
    private Long receiveuserId;
    private boolean isRead;

    @Temporal(TemporalType.TIME)
    @Column(updatable = false)
    private Date createdAtTime;

    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date createdAtDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @PrePersist
    private void onCreate() {
        Date now = new Date();
        createdAtDate = now;
        createdAtTime = now;

        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(createdAtDate);
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(createdAtTime);

        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        dateCalendar.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND));

        createdAt = dateCalendar.getTime();
    }
}