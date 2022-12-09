package com.leavemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.Leave;

@Repository
public interface LeaveRepo extends JpaRepository<Leave, Long> {
	List<Leave> findByEmpName(String empName);

	List<Leave> findByEmpId(String empId);
}
