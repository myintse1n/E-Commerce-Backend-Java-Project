package com.shopping.dto;

import java.math.BigDecimal;
import java.util.List;

import com.shopping.model.Category;

import lombok.Data;

@Data
public class ProductDto {

	private String name;

	private String brand;

	private BigDecimal price;

	private int inventory;

	private String description;

	private List<ImageDto> images;

	private Category category;
}
