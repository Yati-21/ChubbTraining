package com.flight.controller;

import com.flight.entity.Booking;
import com.flight.entity.Passenger;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingRequest {
    private Booking booking;
    private List<Passenger> passengers;
}
