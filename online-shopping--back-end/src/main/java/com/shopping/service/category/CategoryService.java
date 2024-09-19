package com.shopping.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shopping.exception.AlreadyExistsException;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.model.Category;
import com.shopping.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public Category getCategoryById(Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public Category getCategoryByName(String categoryName) {
		return categoryRepository.findByName(categoryName)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		// Check category exists or not
		// If not , add as a new category
		// If exists, give already have exception
		return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
				.map(categoryRepository::save).orElseThrow(() -> new AlreadyExistsException("Category not found!"));
	}

	@Override
	public Category updateCategory(Category category, Long categoryId) {
		// Get existing category by categoryId
		// Then updateCategory
		return Optional.ofNullable(getCategoryById(categoryId)).map(oldCategory -> {
			oldCategory.setName(category.getName());
			return categoryRepository.save(oldCategory);
		}).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public void deleteCategory(Long categoryId) {
		categoryRepository.findById(categoryId).ifPresentOrElse(categoryRepository::delete, () -> {
			throw new ResourceNotFoundException("Category not found!");
		});
	}
}
