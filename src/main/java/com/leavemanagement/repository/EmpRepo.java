package com.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.Employee;

@Repository
@EnableJpaRepositories
public interface EmpRepo extends JpaRepository<Employee, Long> {
//    Optional<Employee> findEmployeeByEmployeeId(Long employeeId);

//    void deleteEmployeeByEmployeeId(Long employeeId);
}
