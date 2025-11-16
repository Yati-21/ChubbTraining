package com.chubb.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BookingRequest {

    @NotBlank
    public String name;

    @Email
    public String email;

    @Min(value=1, message= "Seats must be at least 1")
    public int seats;
    
    @NotBlank(message = "Meal type is required")
    public String mealType;

    public List<PassengerRequest> passengers;
}