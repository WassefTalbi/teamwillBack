package Esprit.tn.EspritJobGetaway.Entity;

import Esprit.tn.EspritJobGetaway.Enum.StatusApplication;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String coverLetter;
    private String cv;

    @ManyToOne
    private Offre offer;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private StatusApplication statusApplication  ;


}
