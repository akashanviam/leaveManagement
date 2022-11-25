package com.leavemanagement.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
@Data
@Entity
public class TypeOfLeave {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Users users;
	private int earnedLeave;
	private int casualLeave;
	private int sickLeave;
	private int leaveWithoutPay;
	private int maternityLeave;
	private int marriageLeave;
	private int paternityLeave;
}
