package com.shopping.service.product;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.shopping.dto.ImageDto;
import com.shopping.dto.ProductDto;
import com.shopping.exception.AlreadyExistsException;
import com.shopping.exception.ProductNotFoundException;
import com.shopping.model.Category;
import com.shopping.model.Image;
import com.shopping.model.Product;
import com.shopping.repository.CategoryRepository;
import com.shopping.repository.ImageRepository;
import com.shopping.repository.ProductRepository;
import com.shopping.request.AddProductRequest;
import com.shopping.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdcutService implements IProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ImageRepository imageRepository;
	private final ModelMapper modelMapper;

	@Override
	public Product addProduct(AddProductRequest request) {

		// Check category is exist or not
		// If exist, set it as the new product category
		// If not exist, save it as the new category
		// Set it as the new product category

		if (productRepository.existsByNameAndBrand(request.getName(), request.getBrand())) {
			throw new AlreadyExistsException(request.getName() + " is already exist. You can update.");
		}
		Category category = categoryRepository.findByName(request.getCategory().getName()).orElseGet(() -> {
			Category newCategory = new Category(request.getCategory().getName());
			return categoryRepository.save(newCategory);
		});
		request.setCategory(category);
		return productRepository.save(createProduct(request, category));
	}

	@Override
	public void deleteById(Long productId) {
		productRepository.findById(productId).ifPresentOrElse(productRepository::delete, () -> {
			throw new ProductNotFoundException("Product not found!");
		});

	}

	@Override
	public Product updateProduct(ProductUpdateRequest request, Long productId) {

		return productRepository.findById(productId)
				.map(existingProduct -> updateExistingProduct(existingProduct, request)).map(productRepository::save)
				.orElseThrow(() -> new ProductNotFoundException("Product not found!"));
	}

	@Override
	public Product getProductById(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found!"));
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String brand, String name) {
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public List<Product> getProductsByBrandAndCategoryName(String brand, String category) {
		return productRepository.findByBrandAndCategoryName(brand, category);
	}

	@Override
	public List<Product> getProductsByCategoryName(String category) {
		return productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand, name);
	}

	private Product createProduct(AddProductRequest request, Category category) {
		var product = new Product(request.getName(), request.getBrand(), request.getPrice(), request.getInventory(),
				request.getDescription(), category);

		return product;
	}

	private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {

		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setPrice(request.getPrice());
		existingProduct.setInventory(request.getInventory());
		existingProduct.setDescription(request.getDescription());

		Category category = categoryRepository.findByName(request.getCategory().getName()).orElseGet(() -> {
			var newCategory = new Category(request.getCategory().getName());
			return categoryRepository.save(newCategory);
		});
		existingProduct.setCategory(category);

		return existingProduct;
	}

	@Override
	public ProductDto convertToDto(Product product) {
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		List<Image> images = imageRepository.findByProductId(product.getId());
		List<ImageDto> imageDto = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
		productDto.setImages(imageDto);
		return productDto;
	}

	@Override
	public List<ProductDto> getConvertedProducts(List<Product> products) {
		return products.stream().map(this::convertToDto).toList();
	}

}
