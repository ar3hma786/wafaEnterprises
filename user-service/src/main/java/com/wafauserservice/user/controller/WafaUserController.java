package com.wafauserservice.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wafauserservice.user.exception.WafaUserException;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.service.WafaUserService;

@RestController
@RequestMapping("/api")
public class WafaUserController {
    
    @Autowired
    private WafaUserService wafaUserService;
    
    @GetMapping("/welcome/{cardNumber}")
    public ResponseEntity<WafaUser> findByCardNumber(@PathVariable("cardNumber") String cardNumber, @RequestHeader("Authorization") String jwt) throws WafaUserException {
    	jwt = jwt.substring("Bearer ".length());
    	WafaUser wafaUser = wafaUserService.findUserFromJwt(jwt);
        
        if (wafaUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        WafaUser getUser = wafaUserService.findByCardNumber(cardNumber);
        if (getUser != null) {
            return ResponseEntity.ok(getUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
