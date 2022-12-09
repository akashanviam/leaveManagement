package com.leavemanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Data
@Entity
public class TypeOfLeave {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	private int earnedLeave;
	private int casualLeave;
	private int sickLeave;
	private int leaveWithoutPay;
	private int maternityLeave;
	private int marriageLeave;
	private int paternityLeave;
}
