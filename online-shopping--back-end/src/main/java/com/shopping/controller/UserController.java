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
import com.shopping.request.CreateUserRequest;
import com.shopping.request.UserUpdateRequest;
import com.shopping.response.ApiResponse;
import com.shopping.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

	private final IUserService userService;

	@GetMapping("/user")
	public ResponseEntity<ApiResponse> getUserById(@RequestParam Long userId) {
		try {
			var user = userService.getUserById(userId);
			var userDto = userService.convertUserToDto(user);
			return ResponseEntity.ok(new ApiResponse("User found!", userDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PostMapping("user/add")
	public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
		try {
			var createUser = userService.createUser(request);
			var createUserDto = userService.convertUserToDto(createUser);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse("Create user success!", createUserDto));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("user/{userId}/update")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
		try {
			var updateUser = userService.updateUser(request, userId);
			var updateUserDto = userService.convertUserToDto(updateUser);
			return ResponseEntity.ok(new ApiResponse("Update user success!", updateUserDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@DeleteMapping("user/{userId}/delete")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok(new ApiResponse("Delete user success!", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

}
