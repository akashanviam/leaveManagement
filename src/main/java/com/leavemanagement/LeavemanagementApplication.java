package com.leavemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* This project use for leave apply
 * @author Akash kumar
*/
@SpringBootApplication
public class LeavemanagementApplication{
//  http://localhost:9000/leave-management/swagger-ui.html   --Swagger URL
	public static void main(String[] args) {
		SpringApplication.run(LeavemanagementApplication.class, args);
	}
}
