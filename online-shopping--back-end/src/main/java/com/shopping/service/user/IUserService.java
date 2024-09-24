package com.shopping.service.user;

import com.shopping.dto.UserDto;
import com.shopping.model.User;
import com.shopping.request.CreateUserRequest;
import com.shopping.request.UserUpdateRequest;

public interface IUserService {

	User getUserById(Long userId);
	User createUser(CreateUserRequest request);
	User updateUser(UserUpdateRequest request, Long userId);
	void deleteUser(Long userId);
	UserDto convertUserToDto(User user);
	User getAuthenticatedUser();
}
