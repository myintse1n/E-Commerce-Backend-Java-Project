package com.shopping.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.shopping.dto.OrderDto;
import com.shopping.enums.OrderStatus;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.model.Cart;
import com.shopping.model.Order;
import com.shopping.model.OrderItem;
import com.shopping.model.Product;
import com.shopping.repository.CartRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.ProductRepository;
import com.shopping.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final ICartService cartService;
	private final ModelMapper modelMapper;

	@Override
	public Order placeOrder(Long userId) {
		// Get cart by userId
		// create new Order
		// set Order properties(date,amount, status, orderItems)

		Cart cart = cartRepository.findByUserId(userId);
		Order order = createOrder(cart);
		List<OrderItem> orderItemList = createOrderItems(order, cart);
		order.setOrderItems(new HashSet<>(orderItemList));
		order.setTotalAmount(calculateToalAmount(orderItemList));
		var saveOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());

		return saveOrder;
	}

	private Order createOrder(Cart cart) {
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderDate(LocalDate.now());
		order.setOrderStaus(OrderStatus.PENDING);
		return order;
	}

	private List<OrderItem> createOrderItems(Order order, Cart cart) {

		return cart.getItems().stream().map(cartItem -> {
			Product product = cartItem.getProduct();
			product.setInventory(product.getInventory() - cartItem.getQuantity());
			productRepository.save(product);
			return new OrderItem(product, order, cartItem.getQuantity(), cartItem.getUnitPrice());
		}).toList();
	}

	private BigDecimal calculateToalAmount(List<OrderItem> orderItemList) {
		return orderItemList.stream()
				.map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public OrderDto getOrder(Long orderId) {
		return orderRepository.findById(orderId).map(this::converOrderToDto)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
	}

	@Override
	public List<OrderDto> getOrders(Long userId) {
		return orderRepository.findByUserId(userId).stream().map(this::converOrderToDto).toList();
	}

	@Override
	public OrderDto converOrderToDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}

}
