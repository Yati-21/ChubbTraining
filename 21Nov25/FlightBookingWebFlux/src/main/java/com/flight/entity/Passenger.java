package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Table("passenger")
@Getter
@Setter
@NoArgsConstructor
public class Passenger {

    @Id
    private Long id;

    @NotBlank
    private String name;

    @Pattern(regexp = "[MFO]", message = "Gender must be M/F/O")
    private String gender;

    @Min(1)
    @Max(120)
    private int age;

    @Pattern(regexp = "^[A-Z]\\d+$", message = "Invalid Seat format")
    private String seatNumber;

    // manual references:
    private Long bookingId;
    private Long flightId;
}
