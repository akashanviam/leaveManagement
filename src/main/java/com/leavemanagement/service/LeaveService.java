package com.leavemanagement.service;

import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.leavemanagement.config.CommonConfig;
import com.leavemanagement.controller.LeaveController;
import com.leavemanagement.dectionary.LeaveStatus;
import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.Leave;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.repository.EmpRepo;
import com.leavemanagement.repository.LeaveRepo;
import com.leavemanagement.utils.CommonUtils;

@Service
public class LeaveService {

	@Autowired
	private LeaveRepo leaveRepo;
	private final Logger LOGGER = LoggerFactory.getLogger(LeaveController.class);

	@Autowired
	private CommonConfig config;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private EmpRepo repo;

	/**
	 * @param leave
	 * @return FinalResponse with leaveStatus, sendVerification email, DateFormats
	 */
	public ResponseEntity<FinalResponse> getCreate(Leave leave, String siteURL) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.debug("getCreate[START] input parameters:" + leave.toString());
		FinalResponse validation = validation(leave);
		try {
			if (validation.getMessage() != null) {
				return new ResponseEntity<FinalResponse>(validation, HttpStatus.NO_CONTENT);
			} else {
				String findDifference = findDifference(leave.getToDate(), leave.getFromDate());
				Employee empl = repo.findByNameLike(leave.getEmpName());
				leave.setEmpId(empl.getEmpId());
				leave.setDuration(findDifference);
				leave.setLeaveStatus(LeaveStatus.PENDING);
				sendVerificationEmail(leave, siteURL);
				Leave save = leaveRepo.save(leave);
				finalResponse.setData(save);
				finalResponse.setMessage(config.getLeaveSuccess());
				finalResponse.setStatus(true);
				LOGGER.debug("getCreate[END] input parameters:" + leave.toString());
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.CREATED);
			}
		} catch (MailAuthenticationException e) {
			finalResponse.setMessage("failed to connect, no password specified!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(config.getLeaveCreateExce() + e.getMessage());
			finalResponse.setStatus(Boolean.FALSE);
			finalResponse.setMessage(config.getLeaveCreateExce());
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String findDifference(String start_date, String end_date) throws ParseException {
		LOGGER.debug("findDifference[START] ");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date d1 = sdf.parse(start_date);
		Date d2 = sdf.parse(end_date);
		long difference_In_Time = d2.getTime() - d1.getTime();
		String differenceInTimeStr = Long.toString(difference_In_Time);
		try {
			Format f = new SimpleDateFormat("EEEE");
			long hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time);
			long days = TimeUnit.MILLISECONDS.toDays(difference_In_Time);
			String str1MonAndFriTo = f.format(d1);
			String str2MonAndFriFrom = f.format(d2);
			long daysAdd = days + config.getTwo();
			if (days == 0) {
				String str = Long.toString(hours);
				return str + " Hours";
			} else if (str1MonAndFriTo.equals("Monday") || str1MonAndFriTo.equals("Friday")
					|| str2MonAndFriFrom.equals("Monday") || str2MonAndFriFrom.equals("Friday")) {
				String string = Long.toString(daysAdd);
				return string + " Days";
			} else {
				String str = Long.toString(days);
				return str + " Days";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(config.getLeaveCreateExce() + e.getMessage());
		}
		LOGGER.debug("findDifference[END]");
		return differenceInTimeStr;
	}

	private FinalResponse validation(Leave leave) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.debug("validation[START] ");
		if (CommonUtils.isNullOrEmpty(leave.getReason())) {
			finalResponse.setMessage("Leave Description is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getLeaveType())) {
			finalResponse.setMessage("Leave Type is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getFromDate())) {
			finalResponse.setMessage("from Date is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getToDate())) {
			finalResponse.setMessage("to Date is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getAlterNativeResource())) {
			finalResponse.setMessage("Alter native resourse name is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getDepartment())) {
			finalResponse.setMessage("Department is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getEmpName())) {
			finalResponse.setMessage("Employee Name is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getTeamLeader())) {
			finalResponse.setMessage("Team Leader Name is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		}
		LOGGER.debug("validation[END] ");
		return finalResponse;
	}

	private void sendVerificationEmail(Leave leave, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		LOGGER.info("Email sending[Start] Got the result");
		Employee teamLeader = repo.findByNameLike(leave.getTeamLeader());
		Employee alterNativeResourse = repo.findByNameLike(leave.getAlterNativeResource());
		String toAddress = teamLeader.getEmail();
		Employee employee = repo.findByNameLike(leave.getEmpName());
		String sender = employee.getEmail();
//		String senderName = leave.getEmpName();
		String subject = "Leave for " + leave.getLeaveType();
		String[] cc = { alterNativeResourse.getEmail(), config.getHeadQuraterMail() };
		String content = "Dear [[name]] sir,<br><br>" + leave.getReason() + "<br>";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(new InternetAddress(sender));
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setCc(cc);
		content = content.replace("[[name]]", leave.getTeamLeader());
		helper.setText(content, true);
		mailSender.send(message);
		LOGGER.debug("Email sending[END]");
	}

	/**
	 * @return list of leave with paging and sorting
	 * @Param offSet
	 */
	public ResponseEntity<Page<Leave>> fetchLeaveByList(int offset, int size, String field) {
		LOGGER.debug("fetchLeaveByList[START-END] ");
		Page<Leave> findAll = leaveRepo.findAll(PageRequest.of(offset, size).withSort(Sort.by(Direction.DESC, field)));
		if (findAll.getSize() <= 0) {
			return new ResponseEntity<Page<Leave>>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Page<Leave>>(findAll, HttpStatus.OK);
		}
	}

	/**
	 * @return leave with id
	 * @PathVariable id
	 */
	public ResponseEntity<FinalResponse> fetchLeaveById(Long id) throws LeaveNotFoundException {
		FinalResponse finalResponse = new FinalResponse();
		Optional<Leave> leave = leaveRepo.findById(id);
		LOGGER.debug("fetchLeaveById[START] ");
		if (!leave.isPresent()) {
			finalResponse.setMessage("Leave is not found");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_FOUND);
		} else {
			LOGGER.debug("fetchLeaveById[END] ");
			finalResponse.setData(leave);
			finalResponse.setMessage("Leave successfully getting");
			finalResponse.setStatus(true);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
		}
	}

	/**
	 * @return list if leave with empId
	 * @PathVariable empId
	 */
	public List<Leave> getLeaveByEmpId(String empId) {
		// TODO Auto-generated method stub
		LOGGER.debug(" getLeaveByEmpId[START-END] ");
		return leaveRepo.findByEmpName(empId);
	}

	/**
	 * @PathVariable empId
	 * @return finalResponse with empId leave is exists or not
	 */
	public ResponseEntity<FinalResponse> fetchLeaveByEmpId(String empId) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.debug("fetchLeaveByEmpId[START]");
		List<Leave> find = leaveRepo.findByEmpId(empId);
		if (find.size() <= 0) {
			finalResponse.setMessage("Leave is not exists !");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(HttpStatus.NOT_FOUND);
		} else {
			finalResponse.setMessage("Leave is  exists !");
			finalResponse.setStatus(true);
			finalResponse.setData(find);
			LOGGER.debug("fetchLeaveByEmpId[END]");
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
		}
	}

	/**
	 * @Path leave Id
	 * @path action - approved or reject
	 * @return finalResponse with leave status changes
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public ResponseEntity<FinalResponse> getLeaveStatusUpdate(String action, long id) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.debug("getLeaveStatusUpdate[START]");
		if (!leaveRepo.existsById(id)) {
			finalResponse.setMessage("Id is not exists!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(HttpStatus.NOT_FOUND);
		}
		try {
			Leave leave = leaveRepo.findById(id).get();
			if (action.equalsIgnoreCase("approved")) {
				leave.setLeaveStatus(LeaveStatus.APPROVED);
				sendApproveAndRejectEmail(leave);
				Leave update = leaveRepo.save(leave);
				finalResponse.setMessage("Leave is Approved !");
				finalResponse.setStatus(true);
				finalResponse.setData(update);
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
			} else if (action.equalsIgnoreCase("reject")) {
				leave.setLeaveStatus(LeaveStatus.REJECTED);
				sendApproveAndRejectEmail(leave);
				Leave update = leaveRepo.save(leave);
				finalResponse.setMessage("Leave is Rejected !");
				finalResponse.setStatus(true);
				finalResponse.setData(update);
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
			}
		} catch (MessagingException e) {
			finalResponse.setMessage("Messages sending exception!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (UnsupportedEncodingException e) {
			finalResponse.setMessage("Unsupported exception!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (MailAuthenticationException e) {
			finalResponse.setMessage("failed to connect, no password specified!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			// TODO: handle exception
			finalResponse.setMessage("Leave Status facing excepton");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<FinalResponse>(HttpStatus.OK);
	}

	private void sendApproveAndRejectEmail(Leave leave) throws MessagingException, UnsupportedEncodingException {
		LOGGER.info("Email sending[Start] for Approve And Reject Got the result");
		Employee teamLeader = repo.findByNameLike(leave.getTeamLeader());
		String teamLeadercc = teamLeader.getEmail();
		Employee employee = repo.findByNameLike(leave.getEmpName());
		String from = "no-reply@anviam.com";
		String subject = "HRM leave is " + leave.getLeaveStatus();
		String[] cc = { teamLeadercc, config.getHeadQuraterMail() };
		String content = "Dear [[name]] sir,<br><br>" + "Your " + leave.getLeaveType() + " Leave is "
				+ leave.getLeaveStatus() + "<br>";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(new InternetAddress(from));
		helper.setTo(employee.getEmail());
		helper.setSubject(subject);
		helper.setCc(cc);
		content = content.replace("[[name]]", employee.getName());
		helper.setText(content, true);
		mailSender.send(message);
		LOGGER.debug("Email sending[END] for Approve And Reject");
	}
}
