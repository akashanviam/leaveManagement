package com.leavemanagement.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.entity.Department;
import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.Leave;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.service.DeptService;
import com.leavemanagement.service.EmpService;
import com.leavemanagement.service.LeaveService;

@RestController
@RequestMapping("anviam/admin")
public class AdminController {

	@Autowired
	private DeptService deptService;

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private EmpService empService;

	private final Logger Logger = LoggerFactory.getLogger(AdminController.class);

	/**
	 * @RequestBody Department
	 * @return finalResponse
	 */
	@PostMapping("/dept/_create")
	public ResponseEntity<FinalResponse> createDept(@RequestBody Department department) {
		return deptService.createDept(department);
	}

	/**
	 *
	 * @return list of Department
	 */
	@GetMapping("/dept/_getAll")
	public ResponseEntity<Page<Department>> getAllDepartment(@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field", defaultValue = "departmentName") String field) {
		return deptService.getAllDepartment(offSet, pageSize, field);
	}

	/**
	 * @RequestBody Employee
	 * @return finalResponse
	 */
	@PostMapping("/emp/_create")
	public ResponseEntity<FinalResponse> createEmp(@RequestBody Employee employee) {
		Logger.info("inside a save data in EmpController");
		return empService.createEmp(employee);
	}

	/**
	 * @PathVariable id
	 * @return if Id is exists it returns the employee data
	 */
	@GetMapping("emp/{id}")
	public ResponseEntity<FinalResponse> fetchDataByEmpId(@PathVariable("id") Long id) throws LeaveNotFoundException {
		Logger.info("inside a get by id data in EmpController");
		return empService.fetchDataByEmpId(id);
	}

	/**
	 *
	 * @return list of Employee
	 */
	@GetMapping("/emp/view")
	public ResponseEntity<Page<Employee>> fetchDataByList(
			@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field", defaultValue = "name") String field) {
		Logger.info("inside a get data in EmpController");
		return empService.fetchDataByList(offSet, pageSize, field);
	}

	/**
	 * @PathVariable empId
	 * @return finalResponse with empId leave is exists or not
	 */
	@GetMapping("emp/{empId}")
	public ResponseEntity<FinalResponse> fetchLeaveByEmpId(@PathVariable("empId") String empId)
			throws LeaveNotFoundException {
		return leaveService.fetchLeaveByEmpId(empId);
	}

	/**
	 * @Param field
	 * @return list of leave with paging and sorting
	 */
	@GetMapping("/leave/getAll")
	public ResponseEntity<Page<Leave>> fetchLeaveByList(@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field") String field) {
		return leaveService.fetchLeaveByList(offSet, pageSize, field);
	}

	/**
	 * @PathVariable id
	 * @return leave with id
	 */
	@GetMapping("/leave/{id}")
	public ResponseEntity<FinalResponse> fetchLeaveById(@PathVariable("id") Long id) throws LeaveNotFoundException {
		return leaveService.fetchLeaveById(id);
	}

	/**
	 * @Path leave Id
	 * @path action - approved or reject
	 * @return finalResponse with leave status changes
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("/_leave/{id}/{action}")
	public ResponseEntity<FinalResponse> getLeaveStatusUpdate(@PathVariable("action") String action,
			@PathVariable("id") Long id) throws UnsupportedEncodingException, MessagingException {
		return leaveService.getLeaveStatusUpdate(action, id);
	}

	/**
	 * @path action - delete , block or unblock
	 * @Path employee Id
	 * @return finalResponse with Employee
	 */
	@GetMapping("/manage-users/{action}/{id}")
	private ResponseEntity<FinalResponse> manageUsers(@PathVariable("action") String action,
			@PathVariable("id") Long id) {
		return empService.manageUsers(action, id);
	}
}
