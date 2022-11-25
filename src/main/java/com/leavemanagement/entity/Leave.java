package com.leavemanagement.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private Long leaveId;
     private String leaveType;
//     @NotNull(message= "please provide a reason for a leave! ")
     private String leaveDescription;
     private String leaveStatus;
     private String leaveTo;
     private String leaveFrom;
//    @NotNull(message= "Please give a employee id!")
     private int empId;


}
