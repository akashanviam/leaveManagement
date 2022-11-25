package com.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.TypeOfLeave;

@Repository
public interface LeaveRepo extends JpaRepository<TypeOfLeave, Integer> {

}
