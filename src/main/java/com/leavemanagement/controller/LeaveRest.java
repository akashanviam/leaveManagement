package com.leavemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.service.LeaveService;

@RestController
@RequestMapping("/leave")
public class LeaveRest {

	@Autowired
	private LeaveService leaveService;
	
//	@PostMapping("/create")
//	public ResponseEntity<FinalResponse> create(@RequestBody TypeOfLeave leave){
//		FinalResponse create = leaveService.create(leave);
//		if(create!=null) {
//		return ResponseEntity.status(HttpStatus.CREATED).body(create);
//		}else {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(create);
//		}
//	}
}
