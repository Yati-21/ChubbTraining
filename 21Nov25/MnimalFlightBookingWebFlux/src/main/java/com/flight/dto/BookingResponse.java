package com.flight.dto;

import com.flight.entity.Booking;
import com.flight.entity.Passenger;
import com.flight.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Booking booking;
    private List<Passenger> passengers;
    private Flight flight;
}
