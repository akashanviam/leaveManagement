package com.leavemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leavemanagement.repository.LeaveRepo;
import com.leavemanagement.repository.UserLoginRepo;

@Service
public class LeaveService {

	@Autowired
	private LeaveRepo leaveRepo;

	@Autowired
	private UserLoginRepo loginRepo;
//
//	public FinalResponse create(TypeOfLeave leave) {
//		FinalResponse finalResponse = new FinalResponse();
//		try {
//			Users findById = loginRepo.findById(leave.getUsers().getId()).get();
//			if(leave.getUsers().getId()==findById.getId()) {
//				leave.setUsers(findById);
//				TypeOfLeave create = leaveRepo.save(leave);
//				if (create != null) {
//					finalResponse.setData(create);
//					finalResponse.setMessage("success");
//					finalResponse.setStatus(true);
//					return finalResponse;
//				} else {
//					finalResponse.setMessage("Data is not found");
//					finalResponse.setStatus(false);
//					return finalResponse;
//				}
//			}else {
//				finalResponse.setMessage("Data is not found");
//				finalResponse.setStatus(false);
//				return finalResponse;
//			}
//		} catch (Exception e) {
//			e.getMessage();
//		}
//		return finalResponse;
//	}

}
