package Esprit.tn.EspritJobGetaway.Entity;


import Esprit.tn.EspritJobGetaway.Enum.OffreType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String titre;
    String description;
    Date startDate;
    Date endDate;
    Integer duration;

    @Enumerated(EnumType.STRING)
    OffreType offerType;

    boolean active = true;


    @ManyToOne
    User user;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Application> applications;

}
