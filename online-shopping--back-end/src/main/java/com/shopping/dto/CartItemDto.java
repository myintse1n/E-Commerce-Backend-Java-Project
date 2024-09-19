package com.shopping.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {


	private BigDecimal unitPrice;
	
	private int quantity;
	
	private BigDecimal totalPrice;
	
	private String  productName;
	
	private String productBrand;
	
	private String productInventory;
}
