package com.shopping.data;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.shopping.model.User;
import com.shopping.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent>{
	
	private final UserRepository userRepository;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		createDefaultUserIfNotExists();
	}
	
	private void createDefaultUserIfNotExists() {
		for(int i=1; i<=5; i++) {
			var defaultEmail = "user"+i+"@gmail.com";
			if(userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("ms");
			user.setLastName("user"+i);
			user.setEmail(defaultEmail);
			user.setPassword("123456");
			userRepository.save(user);
			System.out.println("Default user"+ i + "created successfully.");
		}
	}

}
