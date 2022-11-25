package com.leavemanagement.controller;

import com.leavemanagement.entity.Leave;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.service.LeaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    private final Logger Logger =
            LoggerFactory.getLogger(LeaveController.class);

    @PostMapping("/leave")
    public Leave createLeave(@RequestBody Leave leave){
        Logger.info("Inside save data in LeaveController");
        return leaveService.createLeave(leave);
    }
    @GetMapping("/leave")
    public List<Leave> fetchLeaveByList(){
        Logger.info("Inside fetch data in LeaveController");
        return leaveService.fetchLeaveByList();
    }
    @GetMapping("/leave/{id}")
     public Optional<Leave> fetchLeaveById(@PathVariable("id") Long id) throws  LeaveNotFoundException {
        Logger.info("Inside fetch data by id in LeaveController");
        return leaveService.fetchLeaveById(id);
     }
     @PutMapping("/leave/{id}")
     public Leave updateLeave(@RequestBody Leave leave){
         Logger.info("Inside update data  in LeaveController");
        return leaveService.updateLeave(leave);
     }
     @DeleteMapping("/leave/{id}")
     public String deleteLeave(@PathVariable("id") Long id){
         Logger.info("Inside delete  data by id in LeaveController");
     return leaveService.deleteLeave(id);
     }

}
