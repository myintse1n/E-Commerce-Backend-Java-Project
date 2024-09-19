package com.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.Product;

public interface ProductRepository extends JpaRepositoryImplementation<Product, Long>{

	List<Product> findByBrand(String brand);

	List<Product> findByBrandAndName(String brand, String name);

	List<Product> findByBrandAndCategoryName(String brand, String category);

	List<Product> findByCategoryName(String category);

	List<Product> findByName(String name);

	Long countByBrandAndName(String brand, String name);

}
