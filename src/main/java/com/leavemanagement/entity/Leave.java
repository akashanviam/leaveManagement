package com.leavemanagement.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.leavemanagement.dectionary.EnumDictionaryPro;
import com.leavemanagement.dectionary.EnumLeaveStatus;
import com.leavemanagement.dectionary.LeaveTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Leave {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	
	@Enumerated(EnumType.STRING)
	private LeaveTypeEnum leaveType;
	
	@NotNull(message = "please provide a reason for a leave! ")
	private String leaveDescription;
	
	@Enumerated(EnumType.STRING)
	private EnumLeaveStatus leaveStatus;
	
	private String leaveTo;
	
	private String leaveFrom;
	
	private String duration;
//	@NotNull(message = "Please give a employee id!")
//	@ManyToOne(cascade = CascadeType.ALL)
//	private Employee empId;

}
