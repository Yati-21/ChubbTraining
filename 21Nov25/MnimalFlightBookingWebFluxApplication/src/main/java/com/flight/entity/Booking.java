package com.flight.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("BOOKING")
public class Booking {

    @Id
    private Integer id;

    private Integer flightId;
    private String pnr;
    private String email;
    private int seats;
    private String meal;          // veg / nonveg
    private String seatNumbers;   // "12A,12B"
}
