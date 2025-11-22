package com.flight.entity;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Table("flight")
@Getter
@Setter
@NoArgsConstructor
public class Flight {

    @Id
    private Long id;

    @NotBlank
    private String airline;

    @NotBlank
    private String flightNumber;

    @NotNull
    private AirportCode fromCity;

    @NotNull
    private AirportCode toCity;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @Min(1)
    private int totalSeats;

    @Min(0)
    private int availableSeats;

    @Min(0)
    private double price;

    @AssertTrue(message = "fromCity and toCity cannot be same")
    public boolean isDifferentCities() {
        return fromCity != null && toCity != null && !fromCity.equals(toCity);
    }

    @AssertTrue(message = "Arrival time must be after departure time")
    public boolean isValidTimes() {
        if (departureTime == null || arrivalTime == null) return true;
        return arrivalTime.isAfter(departureTime);
    }
}
