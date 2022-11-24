package com.leavemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
public class LeavemanagementApplication {

//  http://localhost:9000/leave-management/swagger-ui.html   --Swagger URL
	public static void main(String[] args) {
		SpringApplication.run(LeavemanagementApplication.class, args);
	}

}
