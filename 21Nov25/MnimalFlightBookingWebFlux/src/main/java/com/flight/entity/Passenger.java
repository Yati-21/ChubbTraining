package com.flight.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("PASSENGER")
public class Passenger {
    @Id
    private Integer id;
    private Integer bookingId;
    private String name;
    private Integer age;
}
