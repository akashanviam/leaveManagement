package com.leavemanagement.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.Employee;

@Repository
@EnableJpaRepositories
public interface EmpRepo extends JpaRepository<Employee, Long> {
	Employee findByNameLike(String name);

	@Transactional
	@Modifying
	@Query(value = "update employee set active=false where id=?", nativeQuery = true)
	void blockUser(long id);

	@Transactional
	@Modifying
	@Query(value = "update employee set active=true where id=?", nativeQuery = true)
	void unBlockUser(long id);
}
