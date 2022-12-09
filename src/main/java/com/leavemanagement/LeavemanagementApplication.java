package com.leavemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Aamir Majeed Mir
 * @developer Sheetal Devi
 * @developer Akash kumar
 * @tester Prince Rana
 */

@SpringBootApplication
public class LeavemanagementApplication{
	
	/**
	 * Swagger URL
	 * http://localhost:8080/leave-management/swagger-ui.html
	 */
	public static void main(String[] args) {
		SpringApplication.run(LeavemanagementApplication.class, args);
	}
}
