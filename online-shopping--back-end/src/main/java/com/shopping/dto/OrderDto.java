package com.shopping.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import lombok.Data;

@Data
public class OrderDto {
	
	private Long id;
	
	private Long userId;

	private LocalDate orderDate;
	
	private BigDecimal totalAmount;
	
	private String orderStaus;
	
	private Set<OrderItemDto> orderItems;
}
