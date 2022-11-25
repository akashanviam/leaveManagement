package com.leavemanagement.repository;

import com.leavemanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepo extends JpaRepository<Users, Integer>{
    Users findByEmail(String email);
    Users findUsersByEmailAndPassword(String email,String password);
	Users findByActivationKey(String activationKey);

}
