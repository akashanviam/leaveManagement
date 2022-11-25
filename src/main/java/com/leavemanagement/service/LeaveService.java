package com.leavemanagement.service;

import com.leavemanagement.entity.Leave;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.repository.LeaveRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepo leaveRepo;

    public Leave createLeave(Leave leave) {
        return leaveRepo.save(leave);

    }
    public List<Leave> fetchLeaveByList() {

        return leaveRepo.findAll();
    }

    public Optional<Leave> fetchLeaveById(Long id) throws LeaveNotFoundException {
        Optional<Leave> leave= leaveRepo.findById(id);
        if (!leave.isPresent()){
            throw new LeaveNotFoundException("Leave is not found");
        }
        return leave;

    }

    public Leave updateLeave(Leave leave) {
        return leaveRepo.save(leave);
    }

    public String deleteLeave(Long id) {
        if(!leaveRepo.existsById(id)){
            throw new RuntimeException();
        }
        leaveRepo.deleteById(id);
        return "User deleted successfully";

    }
}
