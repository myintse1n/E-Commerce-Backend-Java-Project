package com.shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.exception.ResourceNotFoundException;
import com.shopping.response.ApiResponse;
import com.shopping.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart")
public class CartController {
	
	private final ICartService cartService;
	
	@GetMapping("{cartId}/my-cart")
	public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){
		try {
			var cart = cartService.getCart(cartId);
			var cartDto = cartService.convertCartToDto(cart);
			return ResponseEntity.ok(new ApiResponse("Success to fetch cart by id", cartDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No cart found!", null));
		}
	}
	
	@DeleteMapping("{cartId}/clear")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
		try {
			cartService.clearCart(cartId);
			return ResponseEntity.ok(new ApiResponse("Remove Cart success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No cart found!", null));
		}
	}
	
	@GetMapping("{cartId}/totalPrice")
	public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long cartId){
		try {
			var totalPrice =  cartService.getTotalPrice(cartId);
			return ResponseEntity.ok(new ApiResponse("Remove Cart success",totalPrice));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No cart found!", null));
		}
	}

}
