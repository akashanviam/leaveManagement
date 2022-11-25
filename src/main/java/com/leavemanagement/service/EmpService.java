package com.leavemanagement.service;

import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.Leave;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.repository.EmpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpService {
    @Autowired
    private EmpRepo empRepo;

    public Employee createEmp(Employee employee) {
        return empRepo.save(employee);
    }

    public List<Employee> fetchDataByList() {
        return empRepo.findAll();
    }

    public Optional<Employee> fetchDataByEmpId(Long employeeId) throws LeaveNotFoundException {
        Optional<Employee> employee= empRepo.findEmployeeByEmployeeId(employeeId);

        if (!employee.isPresent()){
            throw new LeaveNotFoundException("Employee is not found");
        }
        return employee;
    }

    public Employee updateEmp(Employee employee) {

        return empRepo.save(employee);
    }

    public String deleteEmp(Long employeeId) {
        empRepo.deleteById(employeeId);
           return "user deleted successfully";
        }
}

