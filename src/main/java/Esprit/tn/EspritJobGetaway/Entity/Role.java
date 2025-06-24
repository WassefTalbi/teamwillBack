package Esprit.tn.EspritJobGetaway.Entity;

import Esprit.tn.EspritJobGetaway.Enum.RoleType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}
