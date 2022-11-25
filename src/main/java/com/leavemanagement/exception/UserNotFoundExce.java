package com.leavemanagement.exception;

public class UserNotFoundExce extends Exception {

	public UserNotFoundExce(String errorMessage) {
		super("You have entered an invalid username or password");
	}
}
