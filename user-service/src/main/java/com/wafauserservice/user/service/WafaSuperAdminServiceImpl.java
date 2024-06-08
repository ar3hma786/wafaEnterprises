package com.wafauserservice.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wafauserservice.user.config.JwtProvider;
import com.wafauserservice.user.domain.ROLE;
import com.wafauserservice.user.exception.WafaAdminException;
import com.wafauserservice.user.exception.WafaSuperAdminException;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.repository.WafaUserRepository;

@Service
public class WafaSuperAdminServiceImpl extends WafaAdminServiceImpl implements WafaSuperAdminService {
    
    @Autowired
    private WafaUserRepository wafaUserRepository;
    
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public List<WafaUser> getAllAdmins() {
        return wafaUserRepository.findAll().stream()
                .filter(user -> !user.getRole().equals(ROLE.WAFA_USER) && !user.getRole().equals(ROLE.WAFA_SUPER_ADMIN))
                .collect(Collectors.toList());
    }

    @Override
    public WafaUser findAdminByUsername(String username) throws WafaAdminException, WafaSuperAdminException {
        WafaUser user = wafaUserRepository.findByUsername(username);
        if (user == null) {
            throw new WafaAdminException("No admin user found with the username: " + username);
        }
        if (user.getRole().equals(ROLE.WAFA_SUPER_ADMIN)) {
            throw new WafaSuperAdminException("The user is a super admin and cannot be treated as a regular admin.");
        }
        return user;
    }

    @Override
    public void deleteAdminByUsername(String username) throws WafaSuperAdminException, WafaAdminException {
        WafaUser user = findAdminByUsername(username);
        if (user != null) {
            wafaUserRepository.delete(user);
        } else {
            throw new WafaSuperAdminException("No admin user found with the username: " + username);
        }
    }

    @Override
    public WafaUser updateAdmin(WafaUser wafaUser) throws WafaAdminException, WafaSuperAdminException {
        WafaUser existingUser = findAdminByUsername(wafaUser.getUsername());
        if (existingUser != null) {
            existingUser.setPassword(wafaUser.getPassword());
            existingUser.setPhoneNo(wafaUser.getPhoneNo());
            return wafaUserRepository.save(existingUser);
        } else {
            throw new WafaAdminException("No admin user found with the username: " + wafaUser.getUsername());
        }
    }

    @Override
    public WafaUser searchAdminUserByQuery(String query) throws WafaSuperAdminException {
        ROLE userRole = ROLE.WAFA_USER;
        ROLE adminRole = ROLE.WAFA_ADMIN;
        List<WafaUser> users = wafaUserRepository.searchAllByQuery(query, userRole, adminRole);

        if (users.isEmpty()) {
            throw new WafaSuperAdminException("No user or admin found for the given query.");
        }

        return users.get(0); 
    }


    @Override
    public void deleteSuperAdmin(String username) throws WafaSuperAdminException {
        WafaUser user = wafaUserRepository.findByUsername(username);
        if (user != null && user.getRole().equals(ROLE.WAFA_SUPER_ADMIN)) {
            wafaUserRepository.delete(user);
        } else {
            throw new WafaSuperAdminException("No super admin user found with the username: " + username);
        }
    }

	@Override
	public WafaUser findSuperAdminFromJwt(String jwt) {
		     String username = jwtProvider.getUsernameFromJwt(jwt);
		    WafaUser findUser = wafaUserRepository.findByUsernameAndRole(username, ROLE.WAFA_SUPER_ADMIN);
		    return findUser;
	}
}
