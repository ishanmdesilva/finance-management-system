package com.inova.pfms.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inova.pfms.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
}