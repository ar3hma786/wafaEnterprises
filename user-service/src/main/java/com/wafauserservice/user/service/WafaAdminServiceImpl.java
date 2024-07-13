package com.wafauserservice.user.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.wafauserservice.user.config.JwtProvider;
import com.wafauserservice.user.domain.ROLE;
import com.wafauserservice.user.exception.WafaAdminException;
import com.wafauserservice.user.exception.WafaUserException;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.repository.WafaUserRepository;

@Service
@Primary
public class WafaAdminServiceImpl implements WafaAdminService{
	
	@Autowired
	private WafaUserRepository wafaUserRepository;
	
	@Autowired
	private WafaUserService wafaUserService;
	
	@Autowired
	private JwtProvider jwtProvider;


	@Override
	public List<WafaUser> getAllUsers() {
	    return wafaUserRepository.findAll().stream()
	            .filter(user -> !user.getRole().equals(ROLE.WAFA_ADMIN) && !user.getRole().equals(ROLE.WAFA_SUPER_ADMIN))
	            .collect(Collectors.toList());
	}
 
	@Override
	public void deleteByCardNumber(String cardNumber) {
	    ROLE admin = ROLE.WAFA_ADMIN;
	    ROLE superAdmin = ROLE.WAFA_SUPER_ADMIN;
	    wafaUserRepository.deleteByCardNumber(cardNumber, admin, superAdmin);
	}
	
	@Override
	public WafaUser findByCardNumber(String cardNumber) throws WafaAdminException, WafaUserException {
	    return wafaUserService.findByCardNumber(cardNumber);

	}



	@Override
	public WafaUser updateUser(WafaUser user) throws WafaAdminException, WafaUserException {
		if (user != null) {
	        WafaUser wafaUser = wafaUserService.findByCardNumber(user.getCardNumber());

	        if (wafaUser != null) {
	            wafaUser.setName(user.getName());
	            wafaUser.setPassword(user.getPassword());
	            wafaUser.setPhoneNo(user.getPhoneNo());
	            wafaUser.setCareOff1(user.getCareOff1());
	            wafaUser.setCareOff2(user.getCareOff2());
	            wafaUser.setCity(user.getCity());
	            wafaUser.setMonths(user.getMonths());
	            wafaUser.setPaymentUpdated(user.getPaymentUpdated());
	            
	            wafaUserRepository.save(wafaUser);
	            return wafaUser;
	        } else {
	            throw new WafaUserException("User not found with card number: " + user.getCardNumber());
	        }
	    } else {
	        throw new WafaUserException("Invalid user");
	    }
	}


	@Override
	public WafaUser searchUserByQuery(String cardNumber, String city, String careOff, String careOff2, Long phoneNo) throws WafaAdminException {
	    WafaUser user = wafaUserRepository.searchByQuery(
	        ROLE.WAFA_USER, // Ensure we are querying only for WAFA_USER role
	        cardNumber,
	        city,
	        careOff,
	        careOff2,
	        phoneNo
	    );

	    if (user != null) {
	        return user;
	    } else {
	        throw new WafaAdminException("User not found with the given query");
	    }
	}




	@Override
	public WafaUser findAdminFromJwt(String jwt) {
	    String username = jwtProvider.getUsernameFromJwt(jwt);
	    return wafaUserRepository.findByUsernameAndRole(username, ROLE.WAFA_ADMIN);
	}

}
