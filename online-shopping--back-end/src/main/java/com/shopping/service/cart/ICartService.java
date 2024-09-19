package com.shopping.service.cart;

import java.math.BigDecimal;

import com.shopping.dto.CartDto;
import com.shopping.model.Cart;
import com.shopping.model.User;

public interface ICartService {
	
	Cart getCart(Long cartId);
	void clearCart(Long cartId);
	BigDecimal getTotalPrice(Long cartId);
	
	 Cart initializeNewCart(User user);
	 Cart getCartByUserId(Long userId);
	CartDto convertCartToDto(Cart cart);
	
}
