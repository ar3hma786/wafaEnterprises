package com.wafauserservice.user.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
	
	private String message;
	
	private String description;
	
	private LocalDateTime localDateTime;

}
