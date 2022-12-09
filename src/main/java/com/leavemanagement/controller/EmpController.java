package com.leavemanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.service.EmpService;

@RestController
@RequestMapping("/emp")
public class EmpController {

	@Autowired
	private EmpService empService;
	private final Logger Logger = LoggerFactory.getLogger(EmpController.class);

	/**
	 * @RequestBody employee
	 * @return FinalResponse with employee
	 */
	@PostMapping("/_create")
	public ResponseEntity<FinalResponse> createEmp(@RequestBody Employee employee) {
		Logger.info("inside a save data in EmpController");
		return empService.createEmp(employee);
	}

}
