package com.leavemanagement.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.dectionary.LeaveStatus;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.Leave;
import com.leavemanagement.service.LeaveService;

@RestController
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;

	/**
	 * @RequestBody leave ,request
	 * @return FinalResponse with leaveStatus, SiteURL and  request
	 */
	@PostMapping("/apply-leave")
	public ResponseEntity<FinalResponse> getCreate(@RequestBody Leave leave, BindingResult result, HttpServletRequest request) {
		return leaveService.getCreate(leave, getSiteURL(request));
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	/**
	 * @PathVariable empId
	 * @return list if leave with empId
	 */
	@GetMapping("/allLeave/{empId}")
	private List<Leave> getLeaveByEmpId(@PathVariable("empId") String empId) {
		return leaveService.getLeaveByEmpId(empId);
	}
	
	@GetMapping("/test")
	public String getallEnum() {
		LeaveStatus[] values = LeaveStatus.values();
		for(LeaveStatus ex:values) {
			System.out.println(ex.getName());
		}
		return null;
	}

}
