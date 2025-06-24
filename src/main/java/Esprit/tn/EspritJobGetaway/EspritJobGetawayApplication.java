package Esprit.tn.EspritJobGetaway;

import Esprit.tn.EspritJobGetaway.Entity.*;
import Esprit.tn.EspritJobGetaway.Enum.RoleType;
import Esprit.tn.EspritJobGetaway.Repository.*;
import Esprit.tn.EspritJobGetaway.config.RsakeysConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@SpringBootApplication
@EnableConfigurationProperties(RsakeysConfig.class)
@RequiredArgsConstructor
@Transactional
public class EspritJobGetawayApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(EspritJobGetawayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initRolesAndAdmin();

    }

    private void initRolesAndAdmin() {
        if (roleRepository.count() == 0) {
            Stream.of(RoleType.ADMIN, RoleType.USER, RoleType.COMPANY, RoleType.STAFF)
                    .forEach(roleType -> {
                        Role role = new Role();
                        role.setRoleType(roleType);
                        roleRepository.save(role);
                    });

            User admin = new User();
            Role role = roleRepository.findByRoleType(RoleType.ADMIN).get();
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@esprit.tn");
            admin.setPhone("28598343");
            admin.setRoles(List.of(role));
            admin.setEnabled(true);
            admin.setNonLocked(true);
            admin.setPassword(passwordEncoder.encode("testTEST#7050"));
            userRepository.save(admin);
        }
    }


}