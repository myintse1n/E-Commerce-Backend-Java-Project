package com.shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.dto.OrderDto;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.response.ApiResponse;
import com.shopping.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

	private final IOrderService orderService;

	@PostMapping("/order")
	public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId) {
		try {
			var order = orderService.placeOrder(userId);
			OrderDto orderDto = orderService.converOrderToDto(order);
			return ResponseEntity.ok(new ApiResponse("Item Order success!", orderDto));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Error occured!", e.getMessage()));
		}
	}

	@GetMapping("/{orderId}/order")
	public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
		try {
			var order = orderService.getOrder(orderId);
			return ResponseEntity.ok(new ApiResponse("Fetch order success!", order));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/{userId}/order")
	public ResponseEntity<ApiResponse> getOrders(@PathVariable Long userId) {
		try {
			var order = orderService.getOrders(userId);
			return ResponseEntity.ok(new ApiResponse("Fetch order success!", order));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
}
