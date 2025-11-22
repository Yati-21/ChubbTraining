package com.flight.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("FLIGHT")
public class Flight {
    @Id
    private Integer id;
    private String flightNumber;
    private String origin;
    private String destination;
    private String departureTime;
}
