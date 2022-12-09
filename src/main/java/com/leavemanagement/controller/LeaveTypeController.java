package com.leavemanagement.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaveTypeController {

	@PostMapping("/_create")
	public void createEmp(@RequestParam String leaveType) {
		SingleConnectionDataSource ds = new SingleConnectionDataSource();
		ds.setDriverClassName("org.postgresql.Driver");
		ds.setUrl("jdbc:postgresql://localhost:5432/anviam-leave");
		ds.setUsername("postgres");
		ds.setPassword("postgres");
		JdbcTemplate jt = new JdbcTemplate(ds);
//		jt.execute("create table leaveTypeTest (id int, "+leaveType+" int , PRIMARY KEY (ID))");
		String check="select leaveTypeTest from leaveTypeTest where leaveType = "+leaveType;
		if(leaveType.equalsIgnoreCase(check)) {
			
			jt.execute("create table leaveTypeTest (id int, "+leaveType+" int , PRIMARY KEY (ID))");
		}else {
			jt.execute("ALTER TABLE leaveTypeTest ADD "+leaveType+" int");
		}
	}
}
