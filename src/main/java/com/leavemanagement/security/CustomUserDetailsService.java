package com.leavemanagement.security;

import java.util.stream.Collectors;

import com.leavemanagement.entity.Users;
import com.leavemanagement.repository.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserLoginRepo userRepository;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

		Users user = userRepository.findByEmail(usernameOrEmail);
		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName()))
							.collect(Collectors.toList()));
		} else {
			throw new UsernameNotFoundException("Invalid email or password");
		}
	}
}
