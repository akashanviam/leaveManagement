package com.leavemanagement.controller;

import com.leavemanagement.entity.Employee;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.service.EmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmpController {

    @Autowired
    private EmpService empService;
    private final Logger Logger =
            LoggerFactory.getLogger(EmpController.class);

    @PostMapping("/emp")
    public Employee createEmp(@RequestBody Employee employee){
        Logger.info("inside a save data in EmpController");

        return  empService.createEmp(employee);
    }
    @GetMapping("/emp")
    public List<Employee> fetchDataByList(){
        Logger.info("inside a get data in EmpController");
        return empService.fetchDataByList();
    }
    @GetMapping("/emp/{employeeId}")
    public Optional<Employee> fetchDataByEmpId(@PathVariable("employeeId") Long employeeId) throws LeaveNotFoundException {
        Logger.info("inside a get by id data in EmpController");
        return  empService.fetchDataByEmpId(employeeId);
    }
    @PutMapping("/emp/{id}")
    public Employee updateEmp(@RequestBody Employee employee){
        Logger.info("inside a update data in EmpController");
        return  empService.updateEmp(employee);
    }
    @DeleteMapping("/emp/{employeeId}")
    public String  deleteEmp(@PathVariable("employeeId") Long employeeId){
        Logger.info("inside a delete data in EmpController");
        return empService.deleteEmp(employeeId);
    }
}

