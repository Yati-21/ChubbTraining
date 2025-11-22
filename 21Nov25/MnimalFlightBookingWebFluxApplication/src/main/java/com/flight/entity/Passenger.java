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
@Table("PASSENGER")
public class Passenger {

    @Id
    private Integer id;

    private Integer bookingId;
    private String name;
    private String gender;
    private int age;
}
