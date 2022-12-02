package com.leavemanagement.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @NotNull(message="Please enter your Mobile number!")
    @Column(length = 10, unique = true, nullable = false)
    private String mobileNumber;
    @Column(unique = true, nullable = false)
    @Email
    private String email;
    private String address;
    private String dob;
    private String empId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "leave_cl", joinColumns = {@JoinColumn(name = "emp_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "leave_id", referencedColumnName = "id") })
	private Leave leave; 
}
