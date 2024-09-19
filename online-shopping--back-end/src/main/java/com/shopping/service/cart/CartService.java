package com.shopping.service.cart;

import java.math.BigDecimal;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopping.dto.CartDto;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.model.Cart;
import com.shopping.model.User;
import com.shopping.repository.CartItemRepository;
import com.shopping.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public Cart getCart(Long cartId) {
		return cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found at id : "+cartId));
	}

	@Transactional
	@Override
	public void clearCart(Long cartId) {
		var cart = getCart(cartId);
		cartItemRepository.deleteAllByCartId(cartId);
        cart.getItems().clear();
		cartRepository.delete(cart);
	}

	@Override
	public BigDecimal getTotalPrice(Long cardId) {
		var cart = getCart(cardId);
		return  cart.getTotalAmount();
	}

	@Override
	public Cart initializeNewCart(User user) {
		return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(()->{
			Cart cart = new Cart();
			cart.setUser(user);
			return cartRepository.save(cart);
		});
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId);
	}

	@Override
	public CartDto convertCartToDto(Cart cart) {
		return modelMapper.map(cart, CartDto.class);
	}
}
