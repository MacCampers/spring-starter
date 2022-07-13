package com.starter.starter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starter.starter.model.*;
import com.starter.starter.payload.requests.UserRequest;
import com.starter.starter.payload.responses.UserResponse;
import com.starter.starter.repository.*;

@RestController
@RequestMapping("/api")
@CrossOrigin()
public class UserController {

        @Autowired
        private PasswordEncoder bcryptEncoder;

        @Autowired
        private UserRepository userRepository;

        /**
         * Gets users by id.
         *
         * @param userId the user id
         * @return the users by id
         * @throws ResourceNotFoundException the resource not found exception
         */
        @GetMapping(value = "/user/{id}")
        public ResponseEntity<UserResponse> getUserById(@PathVariable(value = "id") Long userId)
                        throws UsernameNotFoundException {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found on :: " + userId));
                return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getFirstname(),
                                user.getLastname(), user.getEmail(),
                                user.getRoles()));
        }

        /**
         * Gets all users
         *
         * @return the users
         * @throws ResourceNotFoundException the resource not found exception
         */
        @GetMapping(value = "/users")
        public ResponseEntity<List<UserResponse>> getUsers() {
                List<User> users = userRepository.findAll();
                List<UserResponse> usersReponse = new ArrayList<UserResponse>();
                for (User u : users) {
                        UserResponse uniqueUser = new UserResponse(u.getId(), u.getUsername(),
                                        u.getFirstname(), u.getLastname(), u.getEmail(), u.getRoles());
                        usersReponse.add(uniqueUser);
                }
                return ResponseEntity.ok(usersReponse);
        }

        /**
         * Update user response entity.
         *
         * @return the response entity
         * @throws ResourceNotFoundException the resource not found exception
         */

        @PostMapping(value = "/user")
        public ResponseEntity<User> updateUser(@RequestBody UserRequest userDetails)
                        throws UsernameNotFoundException {

                User user = userRepository.findById(userDetails.getId()).orElseThrow(
                                () -> new UsernameNotFoundException("User not found on :: " + userDetails.getId()));

                if (bcryptEncoder.matches(userDetails.getOldPassword(), user.getPassword())) {
                        user.setPassword(bcryptEncoder.encode(userDetails.getNewPassword()));
                }
                user.setUsername(userDetails.getUsername());
                user.setFirstname(userDetails.getFirstname());
                user.setLastname(userDetails.getLastname());
                user.setEmail(userDetails.getEmail());
                final User updatedUser = userRepository.save(user);
                return ResponseEntity.ok(updatedUser);
        }

        /**
         * Delete user.
         *
         * @param userId the user id
         * @return the map
         * @throws Exception the exception
         */
        @DeleteMapping(value = "/user/{id}")
        public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws UsernameNotFoundException {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found on :: " + userId));
                user.getRoles().clear();
                userRepository.delete(user);
                Map<String, Boolean> response = new HashMap<>();
                response.put("deleted", Boolean.TRUE);
                return response;
        }
}
