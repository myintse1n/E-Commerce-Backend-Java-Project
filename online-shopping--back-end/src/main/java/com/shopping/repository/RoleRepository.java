package com.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.Role;

public interface RoleRepository extends JpaRepositoryImplementation<Role, Long>{
	Optional<Role> findByName(String name);
}
