package com.wafauserservice.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wafauserservice.user.exception.WafaAdminException;
import com.wafauserservice.user.exception.WafaUserException;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.service.WafaAdminService;

@RestController
@RequestMapping("/api/admin")
public class WafaAdminController {
    
    @Autowired
    private WafaAdminService wafaAdminService;
    
 
    
    @GetMapping("/get-all-users")
    public ResponseEntity<List<WafaUser>> getAllUsers(@RequestHeader("Authorization") String jwt) {
   
        WafaUser adminUser = wafaAdminService.findAdminFromJwt(jwt);
        if (adminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    
        List<WafaUser> getAllUsers = wafaAdminService.getAllUsers();
        return new ResponseEntity<>(getAllUsers, HttpStatus.OK);
    }
    
    @GetMapping("/cardNumber/{cardNumber}")
    public ResponseEntity<WafaUser> findByCardNumber(@PathVariable String cardNumber, @RequestHeader("Authorization") String jwt) throws WafaAdminException, WafaUserException {

        WafaUser adminUser = wafaAdminService.findAdminFromJwt(jwt);
        if (adminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        WafaUser wafaUser = wafaAdminService.findByCardNumber(cardNumber);
        return new ResponseEntity<>(wafaUser, HttpStatus.OK);
    }
    
    @GetMapping("/delete/{cardNumber}")
    public ResponseEntity<String> deleteByCardNumber(@PathVariable String cardNumber, @RequestHeader("Authorization") String jwt) throws WafaAdminException, WafaUserException {
    	
        WafaUser adminUser = wafaAdminService.findAdminFromJwt(jwt);
        if (adminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        wafaAdminService.deleteByCardNumber(cardNumber);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/update")
    public ResponseEntity<WafaUser> updateUser(WafaUser wafaUser, @RequestHeader("Authorization") String jwt) throws WafaAdminException, WafaUserException {
    	
        WafaUser adminUser = wafaAdminService.findAdminFromJwt(jwt);
        if (adminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        WafaUser updatedUser = wafaAdminService.updateUser(wafaUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<WafaUser> searchUserByQuery(String cardNumber, String city, String careOff, String careOff2, Long phoneNumber, @RequestHeader("Authorization") String jwt) throws WafaAdminException {
  
        WafaUser adminUser = wafaAdminService.findAdminFromJwt(jwt);
        if (adminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        WafaUser user = wafaAdminService.searchUserByQuery(cardNumber, city, careOff, careOff2, phoneNumber);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
