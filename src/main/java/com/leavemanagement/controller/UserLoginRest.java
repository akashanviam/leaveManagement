package com.leavemanagement.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.dto.UserDto;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.Users;
import com.leavemanagement.exception.UserNotFoundExce;
import com.leavemanagement.service.UserLoginService;

@RestController
@RequestMapping("/users")
public class UserLoginRest {

	@Autowired
	private UserLoginService userService;

	// handler method to handle login request
	@PostMapping("/login")
	public FinalResponse login(String email, String password) throws UserNotFoundExce {
		return userService.login(email, password);
	}

	// handler method to handle user registration form request
	@PostMapping("/register")
	public ResponseEntity<FinalResponse> registration(@Valid @RequestBody UserDto userDto, BindingResult result,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		return userService.saveUser(userDto, getSiteURL(request));
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@GetMapping("/verify")
	public String verifyUser(@Param("activationKey") String activationKey) {
		if (userService.verify(activationKey)) {
			return "verify_success";
		} else {
			return "verify_fail";
		}
	}
	
	@SuppressWarnings("static-access")
	@GetMapping("/userDetails")
	public ResponseEntity<List<UserDto>> findAll() {
		List<UserDto> findAllUsers = userService.findAllUsers();
		if(findAllUsers.size()<=0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			return ResponseEntity.of(Optional.of(findAllUsers)).ok(findAllUsers);
		}
		
	}
}
