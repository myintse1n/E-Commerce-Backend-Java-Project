package com.shopping.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopping.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String email;
	
	private String password;
	
	private Collection<GrantedAuthority> authorities;
	
	public static ShopUserDetails  buildUserDetails(User user) {
		
		List<GrantedAuthority> authorities = user.getRoles().stream().
				map(authority-> new SimpleGrantedAuthority(authority.getName()))
				.collect(Collectors.toList());
		
		return new ShopUserDetails(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

}
