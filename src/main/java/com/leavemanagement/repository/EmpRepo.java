package com.leavemanagement.repository;

import com.leavemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByEmployeeId(Long employeeId);

    void deleteEmployeeByEmployeeId(Long employeeId);
}
