package Esprit.tn.EspritJobGetaway.dto.response;

import Esprit.tn.EspritJobGetaway.Entity.Role;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Enum.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.stream.Collectors;

@Data @NoArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleType> roles;
    public UserResponse(User user){
        this.firstName= user.getFirstName();
        this.lastName= user.getLastName();
        this.email=user.getEmail();
        this.roles=user.getRoles().stream().map(Role::getRoleType).collect(Collectors.toList());
    }
}
