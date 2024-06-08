package com.wafauserservice.user.service;

import java.util.List;

import com.wafauserservice.user.exception.WafaAdminException;
import com.wafauserservice.user.exception.WafaSuperAdminException;
import com.wafauserservice.user.model.WafaUser;

public interface WafaSuperAdminService {
	
	public List<WafaUser> getAllAdmins();
	
    public WafaUser findAdminByUsername(String username) throws WafaAdminException, WafaSuperAdminException;
	
	public void deleteAdminByUsername(String username) throws WafaSuperAdminException, WafaAdminException;
	
	public WafaUser updateAdmin(WafaUser wafaUser) throws WafaAdminException, WafaSuperAdminException;
	
	public WafaUser searchAdminUserByQuery(String query)
			throws WafaSuperAdminException;
	
	public void deleteSuperAdmin(String username) throws WafaSuperAdminException;

	public WafaUser findSuperAdminFromJwt(String jwt);

}
