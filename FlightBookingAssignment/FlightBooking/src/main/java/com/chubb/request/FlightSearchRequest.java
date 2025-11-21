package com.chubb.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSearchRequest 
{
	@NotBlank(message="From city is required")
	private String from;
	
	@NotBlank(message="To city is required")
	private String to;
	
	@NotNull(message="Journey date is required")
	private LocalDate journeyDate; 
}
