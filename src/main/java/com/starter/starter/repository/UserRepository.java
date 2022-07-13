package com.starter.starter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.starter.starter.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

	Optional<User> findById(Long id);

    @Query("SELECT c FROM User c WHERE c.email = :email")
	User findByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    List<User> findAll();
    
    @Query(value = "delete from user_roles where user_id= :user_id and role_id= :role_id", nativeQuery = true)
    void deleteRelation(@Param("user_id") Long user_id, @Param("user_id") Long role_id);
}