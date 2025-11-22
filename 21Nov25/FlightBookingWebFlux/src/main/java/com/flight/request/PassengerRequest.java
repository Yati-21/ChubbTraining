package com.flight.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerRequest {

    @NotBlank(message = "Passenger name is required")
    private String name;

    @Pattern(regexp = "[MFO]", message = "Gender must be M/F/O")
    @NotBlank(message = "Gender is required")
    private String gender;

    @Min(value = 0, message = "Age must be positive")
    @Max(120)
    private int age;

    @Pattern(regexp = "^[A-Z]\\d+$", message = "Invalid Seat format")
    @NotBlank(message = "Seat number is required")
    private String seatNumber;
}
