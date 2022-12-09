package com.leavemanagement.dectionary;

import lombok.Getter;

@Getter
public enum LeaveTypeEnum {

	 EARNEDLEAVE("Earned Leave"),
	 CASUALLEAVE("Casual Leave"),
	 SICKLEAVE("Sick Leave"),
	 LEAVEWITHOUTPAY("Leave Without Pay"),
	 MATERNITYLEAVE("Maternity Leave"),
	 MARRIAGELEAVE("Marriage Leave"),
	 PATERNITYLEAVE("PaternityLeave");

	
	private final String key;
	private String value;

	LeaveTypeEnum(String key) {
		// TODO Auto-generated constructor stub
		this.key=key;
	}
	
	
}
