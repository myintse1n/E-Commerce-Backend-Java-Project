package com.shopping.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.User;

public interface UserRepository extends JpaRepositoryImplementation<User, Long>{

	boolean existsByEmail(String email);

}
