package com.leavemanagement.service;

import com.leavemanagement.controller.LeaveController;
import com.leavemanagement.dectionary.EnumLeaveStatus;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.Leave;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.repository.EmpRepo;
import com.leavemanagement.repository.LeaveRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

	@Autowired
	private LeaveRepo leaveRepo;
	private final Logger Logger = LoggerFactory.getLogger(LeaveController.class);

	public FinalResponse create(Leave leave) {
//		checkNullLeave(leave);
		FinalResponse finalResponse = new FinalResponse();
		Logger.info("Start save data in LeaveService");
		try {
			if(leave.getLeaveDescription()==null) {
				finalResponse.setMessage("Description is null !");
				finalResponse.setStatus(false);
				return finalResponse;
			}else if(leave.getLeaveType()==null) {
				finalResponse.setMessage("Leave Type is null !");
				finalResponse.setStatus(false);
				return finalResponse;
			}else if(leave.getLeaveFrom()==null) {
				finalResponse.setMessage("Leave From is null !");
				finalResponse.setStatus(false);
				return finalResponse;
			}else if(leave.getLeaveTo()==null) {
				finalResponse.setMessage("Leave To is null !");
				finalResponse.setStatus(false);
				return finalResponse;
			}else if(leave.getDuration()==null) {
				finalResponse.setMessage("Duration is null !");
				finalResponse.setStatus(false);
				return finalResponse;
			}else {
				leave.setLeaveStatus(EnumLeaveStatus.PENDING);
				Leave save = leaveRepo.save(leave);
				finalResponse.setData(save);
				finalResponse.setMessage("Sucess");
				finalResponse.setStatus(true);
				return finalResponse;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Logger.error("Exception save data in LeaveService");
		}
		Logger.info("End save data in LeaveService");
		return finalResponse;

	}

	public List<Leave> fetchLeaveByList() {

		return leaveRepo.findAll();
	}

	public Optional<Leave> fetchLeaveById(Long id) throws LeaveNotFoundException {
		Optional<Leave> leave = leaveRepo.findById(id);
		if (!leave.isPresent()) {
			throw new LeaveNotFoundException("Leave is not found");
		}
		return leave;

	}

	public Leave updateLeave(Leave leave) {
		return leaveRepo.save(leave);
	}

	public String deleteLeave(Long id) {
		if (!leaveRepo.existsById(id)) {
			throw new RuntimeException();
		}
		leaveRepo.deleteById(id);
		return "User deleted successfully";

	}
}
