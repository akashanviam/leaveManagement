package com.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.Users;

@Repository
public interface UserLoginRepo extends JpaRepository<Users, Integer>{
    Users findByEmail(String email);
    Users findUsersByEmailAndPassword(String email,String password);
	Users findByActivationKey(String activationKey);

}
