package fshproject.breet.infra.security;

import fshproject.breet.entities.Role;
import fshproject.breet.entities.User;
import fshproject.breet.repository.RoleRepository;
import fshproject.breet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class StaffUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public StaffUserConfig(BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleStaff = roleRepository.findByName(Role.Values.STAFF.name());

        var userStaff = userRepository.findByUsername("staff");

        userStaff.ifPresentOrElse(
                (user) -> {
                        System.out.println("User staff already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername("staff");
                    user.setPassword(bCryptPasswordEncoder.encode("password"));
                    user.setRoles(Set.of(roleStaff));
                    userRepository.save(user);
                    System.out.println("User staff created");
                }
        );
    }
}
