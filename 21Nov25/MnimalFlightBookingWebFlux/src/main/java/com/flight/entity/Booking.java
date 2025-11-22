package com.flight.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("BOOKING")
public class Booking {
    @Id
    private Integer id;
    private String pnr;
    private Integer flightId;
    private String bookingDate;
}
