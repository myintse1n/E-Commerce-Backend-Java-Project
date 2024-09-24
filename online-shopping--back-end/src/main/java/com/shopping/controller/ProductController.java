package com.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.dto.ProductDto;
import com.shopping.exception.AlreadyExistsException;
import com.shopping.exception.ProductNotFoundException;
import com.shopping.model.Product;
import com.shopping.request.AddProductRequest;
import com.shopping.request.ProductUpdateRequest;
import com.shopping.response.ApiResponse;
import com.shopping.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	private final IProductService productService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/product/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {

		try {
			var addedProduct = productService.addProduct(product);
			return ResponseEntity.ok(new ApiResponse("Add product success!", addedProduct));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Add product failed!", null));
		}

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/product/delete/{productId}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {

		try {
			productService.deleteById(productId);
			return ResponseEntity.ok(new ApiResponse("Delete Product successful!", null));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse("Product not found with Id : " + productId, null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/product/update/{productId}")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product,
			@PathVariable Long productId) {

		try {
			var updatedProdcut = productService.updateProduct(product, productId);
			return ResponseEntity.ok(new ApiResponse("Update product success!", updatedProdcut));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse("Product not found with Id : " + productId, null));
		}
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {

		try {
			var product = productService.getProductById(productId);
			ProductDto productDto = productService.convertToDto(product);
			return ResponseEntity.ok(new ApiResponse(" product found!", productDto));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse("Product not found with Id : " + productId, null));
		}
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllProducts() {

		List<Product> allProducts = productService.getAllProducts();
		List<ProductDto> allProductsDto = productService.getConvertedProducts(allProducts);
		return ResponseEntity.ok(new ApiResponse("success", allProductsDto));

	}

	@GetMapping("/product/by-brand")
	public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {

		try {
			var products = productService.getProductsByBrand(brand);
			if (products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found!", null));
			}
			List<ProductDto> productDto = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products found by brand", productDto));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by-brand-name")
	public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand,
			@RequestParam String name) {

		try {
			var products = productService.getProductsByBrandAndName(brand, name);
			if (products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found!", null));
			}
			List<ProductDto> productDtos = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products by brand and name", productDtos));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by-brand-category")
	public ResponseEntity<ApiResponse> getProductsByBrandAndCategoryName(@RequestParam String brand,
			@RequestParam String category) {

		try {
			var products = productService.getProductsByBrandAndCategoryName(brand, category);
			if (products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found!", null));
			}
			List<ProductDto> productDtos = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products by brand and category", productDtos));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by-category")
	public ResponseEntity<ApiResponse> getProductsByCategoryName(@RequestParam String category) {

		try {
			var products = productService.getProductsByCategoryName(category);
			List<ProductDto> productDtos = productService.getConvertedProducts(products);
			if (products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found!", null));
			}
			return ResponseEntity.ok(new ApiResponse("Products by category", productDtos));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/name")
	public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {

		try {
			var products = productService.getProductsByName(name);
			List<ProductDto> productDtos = productService.getConvertedProducts(products);
			if (products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found!", null));
			}
			return ResponseEntity.ok(new ApiResponse("Products by name", productDtos));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product")
	public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
			@RequestParam String name) {

		try {
			var productsCount = productService.countProductsByBrandAndName(brand, name);
			return ResponseEntity.ok(new ApiResponse("Products Count", productsCount));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}
}
