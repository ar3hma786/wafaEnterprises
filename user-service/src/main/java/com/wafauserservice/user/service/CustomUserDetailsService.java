package com.wafauserservice.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wafauserservice.user.domain.ROLE;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.repository.WafaUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private WafaUserRepository wafaUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		WafaUser wafaUser = wafaUserRepository.findByUsername(username);
		
		if(wafaUser == null)
		{
			throw new UsernameNotFoundException("Invalid username or password" +username);
		}
		
		ROLE role = wafaUser.getRole();
		
		if(role == null) role = ROLE.WAFA_USER;
		
		System.out.println("role -----  +" +role);
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		authorities.add(new SimpleGrantedAuthority(role.toString()));
		
        return new org.springframework.security.core.userdetails.User(wafaUser.getUsername(), wafaUser.getPassword(), authorities);
		
	}
		
}
		
		

