package com.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.Category;

public interface CategoryRepository extends JpaRepositoryImplementation<Category, Long> {

	Optional<Category> findByName(String name);

	boolean existsByName(String categoryName);

}
