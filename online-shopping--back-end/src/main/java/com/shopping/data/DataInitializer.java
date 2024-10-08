package com.shopping.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shopping.model.Role;
import com.shopping.model.User;
import com.shopping.repository.RoleRepository;
import com.shopping.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder encoder;

	Set<String> defauleRoles = Set.of("ROLE_ADMIN", "ROLE_USER");

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		createDefaultRoleIfNotExists(defauleRoles);
		createDefaultUserIfNotExists();
		createDefaultAdminIfNotExists();
	}

	private void createDefaultRoleIfNotExists(Set<String> roles) {
		roles.stream().filter(role -> roleRepository.findByName(role).isEmpty()).map(Role::new)
				.forEach(roleRepository::save);
	}

	private void createDefaultUserIfNotExists() {
		Role userRole = roleRepository.findByName("ROLE_USER").get();

		for (int i = 1; i <= 5; i++) {
			var defaultEmail = "user" + i + "@gmail.com";
			if (userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("ms");
			user.setLastName("user" + i);
			user.setEmail(defaultEmail);
			user.setPassword(encoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Default user" + i + "created successfully.");
		}
	}

	private void createDefaultAdminIfNotExists() {
		Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();

		for (int i = 1; i <= 2; i++) {
			var defaultEmail = "admin" + i + "@gmail.com";
			if (userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("ms");
			user.setLastName("admin" + i);
			user.setEmail(defaultEmail);
			user.setPassword(encoder.encode("123456"));
			user.setRoles(Set.of(adminRole));
			userRepository.save(user);
			System.out.println("Default admin" + i + "created successfully.");
		}
	}

}
