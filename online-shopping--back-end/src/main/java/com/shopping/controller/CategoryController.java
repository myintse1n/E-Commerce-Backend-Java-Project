package com.shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.exception.AlreadyExistsException;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.model.Category;
import com.shopping.response.ApiResponse;
import com.shopping.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

	private final ICategoryService categoryService;

	@GetMapping("/{categoryId}/category")
	public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
		try {
			var category = categoryService.getCategoryById(categoryId);
			return ResponseEntity.ok(new ApiResponse("Category found!", category));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/category/{categoryName}")
	public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String categoryName) {

		try {
			var category = categoryService.getCategoryByName(categoryName);
			return ResponseEntity.ok(new ApiResponse("Category found", category));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}

	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllCategories() {

		try {
			var allCategories = categoryService.getAllCategories();
			return ResponseEntity.ok(new ApiResponse("Categories found!", allCategories));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Error!", HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	@DeleteMapping("/category/{categoryId}/delete")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {

		try {
			categoryService.deleteCategory(categoryId);
			return ResponseEntity.ok(new ApiResponse("Delete success!", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PostMapping("/category/add")
	public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {

		try {
			var addCategory = categoryService.addCategory(category);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse("Add category success!", addCategory));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/category/update/{categoryId}")
	public ResponseEntity<ApiResponse> updateCategory(@RequestParam Category category, @PathVariable Long categoryId) {
		try {
			var updatedCategory = categoryService.updateCategory(category, categoryId);
			return ResponseEntity.ok(new ApiResponse("Update categor success!", updatedCategory));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}

}
