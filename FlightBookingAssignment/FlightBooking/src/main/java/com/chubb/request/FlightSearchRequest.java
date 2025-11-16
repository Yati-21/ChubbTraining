package com.chubb.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class FlightSearchRequest 
{
	@NotBlank
	public String from;
	@NotBlank
	public String to;
	@NotBlank
	public LocalDate journeyDate; 
}
