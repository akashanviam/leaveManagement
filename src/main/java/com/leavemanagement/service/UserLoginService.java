package com.leavemanagement.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.Role;
import com.leavemanagement.entity.Users;
import com.leavemanagement.repository.RoleRepo;
import com.leavemanagement.repository.UserLoginRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.leavemanagement.controller.EmpController;
import com.leavemanagement.dto.UserDto;
import com.leavemanagement.exception.UserNotFoundExce;

@Service
public class UserLoginService {

	@Autowired
	private UserLoginRepo userRepository;
	@Autowired
	private RoleRepo roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;

	private final Logger Logger = LoggerFactory.getLogger(UserLoginService.class);
//	public Users saveUser(UserDto userDto) {
//		Users existingUser = findUserByEmail(userDto.getEmail());
//		Users user = new Users();
//		user.setName(userDto.getFirstName() + " " + userDto.getLastName());
//		user.setEmail(userDto.getEmail());
//		// encrypt the password using spring security
//		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
////		Role role = roleRepository.findByName("ROLE_ADMIN");
////		if (role == null) {
////			role = checkRoleExist();
////		}
////		user.setRoles(Arrays.asList(role));
//		Users save = userRepository.save(user);
//		return save;
//	}

	public ResponseEntity<FinalResponse> saveUser(UserDto userDto, String siteURL)
			throws UnsupportedEncodingException, MessagingException {
		Logger.info("UserLoginService method saveUser() Start");
		FinalResponse finalResponse = new FinalResponse();
		Users existingUser = findUserByEmail(userDto.getEmail());

		try {
			if (userDto.getFirstName() != null && userDto.getLastName() != null && userDto.getEmail() != null
					&& userDto.getPassword() != null) {
				Users user = new Users();
				user.setName(userDto.getFirstName() + " " + userDto.getLastName());
				user.setEmail(userDto.getEmail());
				user.setPassword(passwordEncoder.encode(userDto.getPassword()));
				user.setActivated(false);
				user.setActivationKey(UUID.randomUUID().toString());
//				List<Role> findAll = roleRepository.findAll();
//
//				user.setRoles(findAll);

				Users save = userRepository.save(user);
				sendVerificationEmail(user, siteURL);
				finalResponse.setStatus(true);
				finalResponse.setMessage("Register successfully");
				finalResponse.setData(save);
				Logger.info("UserLoginService saveUser() method END Data save "+save);
				return ResponseEntity.status(HttpStatus.CREATED).body(finalResponse);
			} else {
				finalResponse.setStatus(false);
				finalResponse.setMessage("Please check your details");
				Logger.error("UserLoginService saveUser() method facing data issues ");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(finalResponse);
			}

		} catch (DataIntegrityViolationException e) {
			finalResponse.setMessage("Email is already exits " + userDto.getEmail() + " " + e.getMessage());
			finalResponse.setStatus(false);
			Logger.error("UserLoginService saveUser() method facing Duplicate Email issues ");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(finalResponse);
		} catch (Exception e) {
			finalResponse.setMessage(e.getLocalizedMessage());
			finalResponse.setStatus(false);
			Logger.error("UserLoginService saveUser() method Exception");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(finalResponse);
		}
	}

	private void sendVerificationEmail(Users user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "akashk2251@gmail.com";
		String senderName = "admin@anviam.com";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>"
				+ "Leave management System";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getName());
		String verifyURL = siteURL + "/users" + "/verify?activationKey=" + user.getActivationKey();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);

	}

	public boolean verify(String activationKey) {
		Users user = userRepository.findByActivationKey(activationKey);

		if (user == null || user.getActivated() == true) {
			return false;
		} else {
			user.setActivationKey(null);
			user.setActivated(true);
			userRepository.save(user);

			return true;
		}
	}

	private Users findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<UserDto> findAllUsers() {
		List<Users> users = userRepository.findAll();
		return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
	}

	private UserDto mapToUserDto(Users user) {
		UserDto userDto = new UserDto();
		String[] str = user.getName().split(" ");
		userDto.setId(user.getId());
		userDto.setFirstName(str[0]);
		userDto.setLastName(str[1]);
		userDto.setEmail(user.getEmail());
		return userDto;
	}

	private Role checkRoleExist() {
		Role role = new Role();
		role.setName("ROLE_ADMIN");
		return roleRepository.save(role);
	}

	public FinalResponse login(String email, String password) throws UserNotFoundExce {
		FinalResponse finalResponse = new FinalResponse();
		// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// password = passwordEncoder.encode(password);
		Users findUsersByEmailAndPassword = userRepository.findUsersByEmailAndPassword(email, password);

		if (email.equals(findUsersByEmailAndPassword.getEmail())
				&& password.equals(findUsersByEmailAndPassword.getPassword())) {
			finalResponse.setData(findUsersByEmailAndPassword);
			finalResponse.setMessage("successfully login");
			finalResponse.setStatus(true);
			return ResponseEntity.accepted().body(finalResponse).getBody();
		}
		if (email.equals(null) || password.equals(null)) {
			finalResponse.setMessage("You have entered an invalid username or password");
			finalResponse.setStatus(false);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(finalResponse).getBody();
		}
		return finalResponse;
	}
}
