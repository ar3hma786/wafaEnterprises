package com.wafauserservice.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wafauserservice.user.exception.WafaAdminException;
import com.wafauserservice.user.exception.WafaSuperAdminException;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.service.WafaSuperAdminService;

@RestController
@RequestMapping("/api/superadmin")
public class WafaSuperAdminController {

    @Autowired
    private WafaSuperAdminService wafaSuperAdminService;

    @GetMapping("/admins")
    public ResponseEntity<List<WafaUser>> getAllAdmins(@RequestHeader("Authorization") String jwt) {
        WafaUser superAdminUser = wafaSuperAdminService.findSuperAdminFromJwt(jwt);
        if (superAdminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<WafaUser> admins = wafaSuperAdminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @GetMapping("/admin/{username}")
    public ResponseEntity<WafaUser> findAdminByUsername(@PathVariable String username, @RequestHeader("Authorization") String jwt) {
        WafaUser superAdminUser = wafaSuperAdminService.findSuperAdminFromJwt(jwt);
        if (superAdminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            WafaUser admin = wafaSuperAdminService.findAdminByUsername(username);
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } catch (WafaAdminException | WafaSuperAdminException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/admin/{username}")
    public ResponseEntity<Void> deleteAdminByUsername(@PathVariable String username, @RequestHeader("Authorization") String jwt) {
        WafaUser superAdminUser = wafaSuperAdminService.findSuperAdminFromJwt(jwt);
        if (superAdminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            wafaSuperAdminService.deleteAdminByUsername(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (WafaAdminException | WafaSuperAdminException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/admin")
    public ResponseEntity<WafaUser> updateAdmin(@RequestBody WafaUser wafaUser, @RequestHeader("Authorization") String jwt) {
        WafaUser superAdminUser = wafaSuperAdminService.findSuperAdminFromJwt(jwt);
        if (superAdminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            WafaUser updatedAdmin = wafaSuperAdminService.updateAdmin(wafaUser);
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        } catch (WafaAdminException | WafaSuperAdminException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<WafaUser> searchAdminUserByQuery(@RequestParam String query, @RequestHeader("Authorization") String jwt) {
        WafaUser superAdminUser = wafaSuperAdminService.findSuperAdminFromJwt(jwt);
        if (superAdminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            WafaUser admin = wafaSuperAdminService.searchAdminUserByQuery(query);
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } catch (WafaSuperAdminException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/superadmin/{username}")
    public ResponseEntity<Void> deleteSuperAdmin(@PathVariable String username, @RequestHeader("Authorization") String jwt) {
        WafaUser superAdminUser = wafaSuperAdminService.findSuperAdminFromJwt(jwt);
        if (superAdminUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            wafaSuperAdminService.deleteSuperAdmin(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (WafaSuperAdminException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
