package com.shopping.service.product;

import java.util.List;

import com.shopping.dto.ProductDto;
import com.shopping.model.Product;
import com.shopping.request.AddProductRequest;
import com.shopping.request.ProductUpdateRequest;

public interface IProductService {

	Product addProduct(AddProductRequest request);
	void deleteById(Long productId);
	Product updateProduct(ProductUpdateRequest request,Long productId);	
	Product getProductById(Long productId);
	
	List<Product> getAllProducts();
	List<Product> getProductsByBrand(String brand);
	List<Product> getProductsByBrandAndName(String brand, String name);
	List<Product> getProductsByBrandAndCategoryName(String brand , String category);
	List<Product> getProductsByCategoryName(String category);
	List<Product> getProductsByName(String name);
	
	Long countProductsByBrandAndName(String brand, String name);
	ProductDto convertToDto(Product product);
	List<ProductDto> getConvertedProducts(List<Product> products);
}
