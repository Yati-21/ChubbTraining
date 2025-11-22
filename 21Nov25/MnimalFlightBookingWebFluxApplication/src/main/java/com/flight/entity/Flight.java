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
@Table("FLIGHT")
public class Flight {

    @Id
    private Integer id;

    private String airline;
    private String flightNumber;
    private String origin;
    private String destination;
    private String date;    // yyyy-MM-dd
    private String time;    // HH:mm
    private float price;
    private float roundTripPrice;
}
