package com.shopping.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.Cart;

public interface CartRepository extends JpaRepositoryImplementation<Cart, Long>{

	Cart findByUserId(Long userId);

}
