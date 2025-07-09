package Esprit.tn.EspritJobGetaway.Entity;

import Esprit.tn.EspritJobGetaway.Enum.StatusApplication;
import Esprit.tn.EspritJobGetaway.Enum.StatusEntretien;
import Esprit.tn.EspritJobGetaway.Enum.TypeEntretien;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Entretien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    TypeEntretien typeEntretien;

    @Temporal(TemporalType.TIMESTAMP)
    Date dateEntretien;

    String lieu;

    @Enumerated(EnumType.STRING)
    StatusEntretien statusEntretien;

    @ManyToOne
    Application application;
}