package com.wafauserservice.user.service;

import java.util.List;

import com.wafauserservice.user.exception.WafaAdminException;
import com.wafauserservice.user.exception.WafaUserException;
import com.wafauserservice.user.model.WafaUser;

public interface WafaAdminService {
	
	public List<WafaUser> getAllUsers();
	
	public WafaUser findByCardNumber(String cardNumber) throws WafaAdminException, WafaUserException;
	
	public void deleteByCardNumber(String cardNumber);
	
	public WafaUser updateUser(WafaUser wafaUser) throws WafaAdminException, WafaUserException;
	

	public WafaUser searchUserByQuery(String cardNumber, String city, String careOff, String careOff2, Long phoneNumber)
			throws WafaAdminException;
	
	public WafaUser findAdminFromJwt(String jwt);
	

}
