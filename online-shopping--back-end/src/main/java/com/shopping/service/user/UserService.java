package com.shopping.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.shopping.dto.UserDto;
import com.shopping.exception.AlreadyExistsException;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.model.User;
import com.shopping.repository.UserRepository;
import com.shopping.request.CreateUserRequest;
import com.shopping.request.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
	}

	@Override
	public User createUser(CreateUserRequest request) {
		return Optional.of(request)
				.filter(user -> !userRepository.existsByEmail(request.getEmail()))
				.map(req->{
					User createUser = new User();
					createUser.setFirstName(request.getFirstName());
					createUser.setLastName(request.getLastName());
					createUser.setEmail(request.getEmail());
					createUser.setPassword(request.getPassword());
					return userRepository.save(createUser);
				}).orElseThrow(()-> new AlreadyExistsException("Oops! "+ request.getEmail() + " already exists!.") );
	}

	@Override
	public User updateUser(UserUpdateRequest request, Long userId) {
		return userRepository.findById(userId)
				.map(existingUser->{
					existingUser.setFirstName(request.getFirstName());
					existingUser.setLastName(request.getLastName());
					return userRepository.save(existingUser);					 
				}).orElseThrow(()-> new ResourceNotFoundException("User not found!"));
	}

	@Override
	public void deleteUser(Long userId) {
		var user = getUserById(userId);
		userRepository.delete(user);
	}
	
	@Override
	public UserDto convertUserToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}

}
