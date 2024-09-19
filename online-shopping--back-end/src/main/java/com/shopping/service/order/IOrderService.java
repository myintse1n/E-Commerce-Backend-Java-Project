package com.shopping.service.order;

import java.util.List;

import com.shopping.dto.OrderDto;
import com.shopping.model.Order;

public interface IOrderService {

	Order placeOrder(Long userId);
	
	OrderDto getOrder(Long orderId);
	
	List<OrderDto> getOrders (Long userId);
	
	OrderDto converOrderToDto(Order order);
	
}
