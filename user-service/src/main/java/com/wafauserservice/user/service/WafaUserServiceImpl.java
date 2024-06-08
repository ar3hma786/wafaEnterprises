package com.wafauserservice.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wafauserservice.user.config.JwtProvider;
import com.wafauserservice.user.domain.ROLE;
import com.wafauserservice.user.exception.WafaUserException;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.repository.WafaUserRepository;

@Service
public class WafaUserServiceImpl implements WafaUserService {
	
	@Autowired
	private WafaUserRepository wafaUserRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	
	public WafaUser findByCardNumber(String cardNumber) throws WafaUserException  {
	        WafaUser getUser = wafaUserRepository.findByCardNumber(cardNumber);
	        if (getUser == null) {
	            throw new WafaUserException("User not found with Card Number " + cardNumber);
	        }
	        return getUser;
	    }


	@Override
	public WafaUser findUserFromJwt(String jwt) {
		String username = jwtProvider.getUsernameFromJwt(jwt);
	    WafaUser findUser = wafaUserRepository.findByUsernameAndRole(username, ROLE.WAFA_USER);
	    return findUser;
	}

}
