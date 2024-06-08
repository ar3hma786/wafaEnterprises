package com.wafauserservice.user.service;


import com.wafauserservice.user.exception.WafaUserException;
import com.wafauserservice.user.model.WafaUser;

public interface WafaUserService {
	

	public WafaUser findByCardNumber(String cardNumber) throws WafaUserException;
	
	public WafaUser findUserFromJwt(String jwt);


}
