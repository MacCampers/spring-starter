package com.starter.starter.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.starter.starter.model.*;
import com.starter.starter.repository.*;

@Configuration
public class DataInitialization {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        /* Create admin  */
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@admin.fr", bcryptEncoder.encode("adminpassword"));
            userRepository.save(admin);
        }

        /* Create roles */
        if (!roleRepository.existsByName("User") && !roleRepository.existsByName("Admin") && !roleRepository.existsByName("Moderator")) {
            User admin = userRepository.findByUsername("admin").get();
            Role role1 = new Role("User");
            Role role2 = new Role("Admin");
            Role role3 = new Role("Moderator");

            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);

            Set<Role> roles = new HashSet<>();
            roles.add(role1);
            roles.add(role2);
            roles.add(role3);
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }
}
