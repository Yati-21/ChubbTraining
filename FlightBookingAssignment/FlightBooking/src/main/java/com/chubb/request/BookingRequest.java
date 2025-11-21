package com.chubb.request;

import java.util.List;

import com.chubb.entity.MealType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest 
{

    @NotBlank(message="Name is required")
    private String name;

    @Email(message="Invalid email format")
    @NotBlank(message="Email is required")
    private String email;

    @Min(value=1,message="at least 1 must be booked")
    private int seats;
    
    @NotNull(message="Meal type is required")
    private MealType mealType;


    @NotEmpty(message="Passenger list cannot be empty")
    private List<PassengerRequest> passengers;
}
