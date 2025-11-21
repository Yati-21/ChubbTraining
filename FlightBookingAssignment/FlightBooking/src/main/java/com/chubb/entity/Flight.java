package com.chubb.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight 
{

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String airline;

    @NotBlank
    @Column(unique= true)
    private String flightNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AirportCode fromCity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AirportCode toCity;

    @NotNull
    private LocalDateTime departureTime;
    
    @NotNull
    private LocalDateTime arrivalTime;

    @Min(value=1)
    private int totalSeats;

    @Min(value=0)
    private int availableSeats;

    @Min(value=0)
    private double price;
    
    @AssertTrue(message ="fromCity and toCity cannot be same")
    public boolean isDifferentCities() 
    {
        return fromCity!= null && toCity!= null && !fromCity.equals(toCity);
    }

    @AssertTrue(message="Arrival time must be after departure time")
    public boolean isValidTimes() 
    {
        if (departureTime==null ||arrivalTime ==null) return true;
        return arrivalTime.isAfter(departureTime);
    }

    
    
}