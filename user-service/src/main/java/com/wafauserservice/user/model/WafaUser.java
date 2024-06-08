package com.wafauserservice.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.wafauserservice.user.domain.ROLE;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wafauser")
public class WafaUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String cardNumber; 
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    private String username; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ROLE role;
    
    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "PhoneNo is required")
    private Long phoneNo;
    
    private String careOff1;
    
    private String careOff2;

    private String city;
    
    private String paymentUpdated;
    
    @ElementCollection
    private List<String> months = new ArrayList<>();
    
}
