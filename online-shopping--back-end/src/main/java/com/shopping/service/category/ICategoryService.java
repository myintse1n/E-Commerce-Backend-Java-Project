package com.shopping.service.category;

import java.util.List;

import com.shopping.model.Category;

public interface ICategoryService {
	
	Category getCategoryById(Long categoryId);
	Category getCategoryByName(String categoryName);
	List<Category> getAllCategories();
	Category addCategory(Category category);
	Category updateCategory(Category category, Long categoryId);
	void deleteCategory(Long categoryId);
	

}
