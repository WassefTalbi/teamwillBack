package Esprit.tn.EspritJobGetaway.Repository;

import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Enum.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User>findByEmail(String email);
    Optional<User>findByPhone(String mobileNumber);
    List<User> findByRolesRoleType(RoleType roleType);

    // Page<User> findByRolesRoleType(RoleType type, Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email = ?1")
    int enableAppUser(String email);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleType IN :roleTypes")
    List<User> findByRolesRoleTypeIn(@Param("roleTypes") List<RoleType> roleTypes);


        Optional<User> findById(Long id);






}
