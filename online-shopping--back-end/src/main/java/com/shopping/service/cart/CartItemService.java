package com.shopping.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.shopping.exception.ResourceNotFoundException;
import com.shopping.model.Cart;
import com.shopping.model.CartItem;
import com.shopping.repository.CartItemRepository;
import com.shopping.repository.CartRepository;
import com.shopping.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final CartRepository cartRepository;
	private final ICartService cartService;

	@Override
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		// 1. Get cart by cartId
		// 2. Check the product exist at cart
		// 3. If exists, update quantity
		// 4. If not , add new cartItem to cart
		Cart cart = cartService.getCart(cartId);
		var product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
		var cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
				.orElse(new CartItem());
		if (cartItem.getId() == null) {
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setUnitPrice(product.getPrice());
			cartItem.setQuantity(quantity);
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		}
		cartItem.setTotalPrice();
		cart.addItem(cartItem);
		cartItemRepository.save(cartItem);
		cartRepository.save(cart);
	}

	@Override
	public void removeItemFromCart(Long cartId, Long productId) {
		var cart = cartService.getCart(cartId);
		var cartIem = getCartItem(cartId, productId);
		cart.removeItem(cartIem);
		cartRepository.save(cart);

	}

	@Override
	public void updateItemQuantity(Long cartId, Long productId, int quantity) {
		Cart cart = cartService.getCart(cartId);
		cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
				.ifPresent(item -> {
					item.setQuantity(quantity);
					item.setUnitPrice(item.getProduct().getPrice());
					item.setTotalPrice();
				});
		BigDecimal totalAmount = cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		cart.setTotalAmount(totalAmount);
		cartRepository.save(cart);
	}

	@Override
	public CartItem getCartItem(Long cartId, Long productId) {
		Cart cart = cartService.getCart(cartId);
        return  cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
	}

}
