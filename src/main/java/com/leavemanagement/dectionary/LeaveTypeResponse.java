package com.leavemanagement.dectionary;

import com.leavemanagement.entity.Leave;

public class LeaveTypeResponse {

	private Leave leave;
	private LeaveTypeEnum[] leaveTypeEnum=LeaveTypeEnum.values();
	public Leave getLeave() {
		return leave;
	}
	public void setLeave(Leave leave) {
		this.leave = leave;
	}
	public LeaveTypeEnum[] getLeaveTypeEnum() {
		return leaveTypeEnum;
	}
	public LeaveTypeResponse(Leave leave) {
		super();
		this.leave = leave;
	}

	
	
}
