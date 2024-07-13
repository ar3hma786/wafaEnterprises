package com.wafauserservice.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wafauserservice.user.config.JwtProvider;
import com.wafauserservice.user.domain.ROLE;
import com.wafauserservice.user.exception.WafaUserException;
import com.wafauserservice.user.model.WafaUser;
import com.wafauserservice.user.repository.WafaUserRepository;
import com.wafauserservice.user.request.AdminLoginRequest;
import com.wafauserservice.user.request.LoginRequest;
import com.wafauserservice.user.response.AuthResponse;
import com.wafauserservice.user.service.CustomUserDetailsService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final WafaUserRepository wafaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetails;

    public AuthController(WafaUserRepository wafaUserRepository, PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider, CustomUserDetailsService customUserDetails) {
        this.wafaUserRepository = wafaUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserDetails = customUserDetails;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody WafaUser user) throws WafaUserException {
        return createHandler(user, ROLE.WAFA_USER);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> createAdminHandler(@RequestBody WafaUser user) throws WafaUserException {
        return createHandler(user, ROLE.WAFA_ADMIN);
    }

    @PostMapping("/register-super-admin")
    public ResponseEntity<AuthResponse> createSuperAdminHandler(@RequestBody WafaUser user) throws WafaUserException {
        return createHandler(user, ROLE.WAFA_SUPER_ADMIN);
    }

    private ResponseEntity<AuthResponse> createHandler(WafaUser user, ROLE role) throws WafaUserException {
        String username = null;
        String password = null;
        String name = null;
        Long phone = null;

        if (role == ROLE.WAFA_USER) {
            username = user.getUsername();
            password = user.getPassword();
            name = user.getName();
            phone = user.getPhoneNo();
        } else if (role == ROLE.WAFA_ADMIN || role == ROLE.WAFA_SUPER_ADMIN) {
            username = user.getUsername();
            password = user.getPassword();
            name = user.getName();
            phone = user.getPhoneNo();
        }

        WafaUser isUsernameExist = wafaUserRepository.findByUsername(username);
        if (isUsernameExist != null) {
            throw new WafaUserException("Username already exists. Please choose a different username.");
        }

        WafaUser createdUser = new WafaUser();
        createdUser.setUsername(username);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setName(name);
        createdUser.setPhoneNo(phone);
        createdUser.setRole(role);

        if (role == ROLE.WAFA_USER) {
            createdUser.setCardNumber(user.getCardNumber());
            createdUser.setCareOff1(user.getCareOff1());
            createdUser.setCareOff2(user.getCareOff2());
            createdUser.setCity(user.getCity());
            createdUser.setMonths(user.getMonths());
        }

        WafaUser savedUser = wafaUserRepository.save(createdUser);

        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("Register Success");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) throws WafaUserException {
        return loginHandler(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/login-admin")
    public ResponseEntity<AuthResponse> adminHandler(@RequestBody AdminLoginRequest adminLoginRequest) throws WafaUserException {
        return loginHandler(adminLoginRequest.getUsername(), adminLoginRequest.getPassword());
    }

    @PostMapping("/login-super-admin")
    public ResponseEntity<AuthResponse> superAdminHandler(@RequestBody AdminLoginRequest adminLoginRequest) throws WafaUserException {
        return loginHandler(adminLoginRequest.getUsername(), adminLoginRequest.getPassword());
    }

    private ResponseEntity<AuthResponse> loginHandler(String username, String password) throws WafaUserException {
        Authentication authentication = authenticateUser(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Success");
        authResponse.setToken(token);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private UsernamePasswordAuthenticationToken authenticateUser(String username, String password) throws WafaUserException {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    private ResponseEntity<AuthResponse> createAuthResponse(String username, String message, HttpStatus status) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage(message);

        return new ResponseEntity<>(authResponse, status);
    }
}
