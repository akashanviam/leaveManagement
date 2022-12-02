package com.leavemanagement.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leavemanagement.dectionary.EnumLeaveStatus;
import com.leavemanagement.dectionary.LeaveTypeEnum;

import lombok.Data;

@Entity
@Data
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date leaveTo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date leaveFrom;

	private String duration;
//	@NotNull(message = "Please give a employee id!")
//	@ManyToOne(cascade = CascadeType.ALL)
//	private Employee empId;

}
