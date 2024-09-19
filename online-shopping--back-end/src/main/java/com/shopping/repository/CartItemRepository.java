package com.shopping.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.CartItem;

public interface CartItemRepository extends JpaRepositoryImplementation<CartItem, Long> {
	
	void deleteAllByCartId(Long cartId);
}
