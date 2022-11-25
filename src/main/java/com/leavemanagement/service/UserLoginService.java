package com.leavemanagement.service;

import java.io.UnsupportedEncodingException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

	public ResponseEntity<FinalResponse> saveUser(UserDto userDto, String siteURL) {
		FinalResponse finalResponse = new FinalResponse();
		Users existingUser = findUserByEmail(userDto.getEmail());

		try {
			Users user = new Users();
			user.setName(userDto.getFirstName() + " " + userDto.getLastName());
			user.setEmail(userDto.getEmail());
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user.setActivated(false);
			user.setActivationKey(UUID.randomUUID().toString());
			sendVerificationEmail(user, siteURL);
			Users save = userRepository.save(user);
			finalResponse.setStatus(true);
			finalResponse.setMessage("Register successfully");
			finalResponse.setData(save);
			return ResponseEntity.status(HttpStatus.CREATED).body(finalResponse);

		} catch (Exception e) {
			finalResponse.setStatus(false);
			finalResponse.setMessage("There is already an account registered with the same email");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(finalResponse);
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

	public Users findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<UserDto> findAllUsers() {
		List<Users> users = userRepository.findAll();
		return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
	}

	private UserDto mapToUserDto(Users user) {
		UserDto userDto = new UserDto();
		String[] str = user.getName().split(" ");
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
