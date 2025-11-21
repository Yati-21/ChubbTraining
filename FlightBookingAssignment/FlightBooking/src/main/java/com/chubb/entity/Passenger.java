package com.chubb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames ={"seatNumber", "booking_id"}))
@Getter
@Setter
public class Passenger 
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    
    @Pattern(regexp="[MFO]", message="Gender must be M/F/O")
    private String gender;
    
    @Min(1)
    @Max(120)
    private int age;
    
    @Pattern(regexp="^[A-Z]\\d+$",message="Invalid Seat format")
    private String seatNumber;

    @ManyToOne
    @JsonIgnore //to prevent infinte loop when get request is made
    @JoinColumn(name="booking_id")  //FK to booking
    private Booking booking;
}