package Esprit.tn.EspritJobGetaway.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String email;
    String password;

    // Common fields
    String phone;
    Boolean nonLocked;
    Boolean enabled;
    @Lob
    String photoProfile;

    // USER role-specific fields
    String firstName;
    String lastName;
    String profession;

    // COMPANY role-specific fields
    String companyName;
    String companyAddress;

    @ManyToMany
    @JsonIgnore
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MailToken> mailTokens;



    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Offre> offres;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Application> applications;

    @JsonIgnore
    @ManyToOne
    User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleType().toString()))
            .collect(Collectors.toList());
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}