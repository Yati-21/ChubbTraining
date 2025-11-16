package com.chubb.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PassengerRequest 
{
	@NotBlank
    public String name;
	@NotBlank
    public String gender;
    @Min(0)
    public int age;
    @NotBlank
    public String seatNumber;
}