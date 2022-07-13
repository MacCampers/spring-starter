package com.starter.starter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starter.starter.model.*;
import com.starter.starter.payload.responses.MessageResponse;
import com.starter.starter.repository.*;

@RestController
@RequestMapping("/api")
@CrossOrigin()
public class RoleController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    /**
     * Gets roles list.
     *
     * @return the roles
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping(value = "/roles")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = (List<Role>) roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    /**
     * Gets roles by user id.
     *
     * @param userId the user id
     * @return the role by user id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping(value = "/role/{id}")
    public ResponseEntity<Set<Role>> getRolesById(@PathVariable(value = "id") Long userId)
            throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found on :: " + userId));

        Set<Role> roles = user.getRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Add a role.
     *
     * @return the role
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PostMapping(value = "/role")
    public ResponseEntity<?> addRole(@RequestBody String role){
        if (!roleRepository.existsByName(role)) {
            Role newRole = new Role(role);
            roleRepository.save(newRole);
            return ResponseEntity.ok(newRole);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Role already exists"));
    }

    /**
     * update role list to user
     *
     * @return the roles
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PostMapping(value = "/role/{id}")
    public ResponseEntity<?> updateRoleToUser(@RequestBody String[] roles, @PathVariable(value = "id") Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found on :: " + userId));
        user.getRoles().clear();
        for (String role: roles) {
            if (roleRepository.existsByName(role)) {
                Role newRole = roleRepository.findByName(role).get();
                user.getRoles().add(newRole);
                userRepository.save(user);
            }
        }
        return ResponseEntity.ok(user.getRoles());
    }

    /**
     * delete a role
     *
     * @return the role
     * @throws ResourceNotFoundException the resource not found exception
     */
    @DeleteMapping(value = "/role")
    public Map<String, Boolean> deleteRole(@RequestBody String role) {
        if (roleRepository.existsByName(role) && role != "Admin") {
            Role newRole = roleRepository.findByName(role).get();
            for (User u : userRepository.findAll()) {
                u.getRoles().remove(newRole);
            }
            roleRepository.delete(newRole);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        }
        return null;
    }

}
