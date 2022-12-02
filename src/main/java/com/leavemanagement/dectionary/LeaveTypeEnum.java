package com.leavemanagement.dectionary;

import lombok.Getter;

@Getter
public enum LeaveTypeEnum {

	 EarnedLeave("EL","Earned Leave"),
	 CasualLeave("CL","Casual Leave"),
	 SickLeave("SL","Sick Leave"),
	 LeaveWithoutPay("LWP","Leave Without Pay"),
	 MaternityLeave("ML","Maternity Leave"),
	 MarriageLeave("MrL","Marriage Leave"),
	 PaternityLeave("PL","PaternityLeave");

	
	private String key;
	private String value;

	LeaveTypeEnum(String key, String value) {
		// TODO Auto-generated constructor stub
		this.key=key;
		this.value=value;
	}
	
	
}
