package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Table("booking")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    private Long id;

    private String pnr;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotNull
    private int seatsBooked;

    private MealType mealType;

    // manual FK to flight table
    private Long flightId;
}
