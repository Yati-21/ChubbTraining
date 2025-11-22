package com.flight.service;

import java.time.LocalDate;

import com.flight.entity.Booking;
import com.flight.entity.Flight;
import com.flight.request.BookingRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightServiceInterface {

    Mono<Flight> addFlight(Flight flight);

    Flux<Flight> searchFlights(String from, String to, LocalDate journeyDate);

    Mono<String> bookTicket(Long flightId, BookingRequest req);

    Mono<Booking> getTicket(String pnr);

    Flux<Booking> getBookingHistory(String email);

    Mono<String> cancelBooking(String pnr);

    Mono<Booking> updateBooking(String pnr, BookingRequest req);
}
