package com.chubb.service;

import java.time.LocalDate;
import java.util.List;

import com.chubb.entity.Booking;
import com.chubb.entity.Flight;
import com.chubb.request.BookingRequest;

public interface FlightServiceInterface {

	Flight addFlight(Flight flight);
	
    List<Flight> searchFlights(String from, String to, LocalDate journeyDate);

    String bookTicket(Long flightId, BookingRequest req);

    Booking getTicket(String pnr);

    List<Booking> getBookingHistory(String email);

    String cancelBooking(String pnr);
    
    Booking updateBooking(String pnr, BookingRequest req);
    
}