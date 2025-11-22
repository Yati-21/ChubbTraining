package com.flight.controller;

import org.springframework.web.bind.annotation.*;
import com.flight.entity.Flight;
import com.flight.entity.Booking;
import com.flight.request.BookingRequest;
import com.flight.request.FlightSearchRequest;
import com.flight.service.FlightServiceInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightServiceInterface service;

    @PostMapping("/airline/inventory/add")
    public Mono<Flight> addFlight(@RequestBody @Valid Flight flight) {
        log.debug("Adding flight: {}", flight.getFlightNumber());
        return service.addFlight(flight);
    }

    @PostMapping("/search")
    public Flux<Flight> search(@Valid @RequestBody FlightSearchRequest request) {
        return service.searchFlights(request.getFrom(), request.getTo(), request.getJourneyDate());
    }

    @PostMapping("/booking/{flightId}")
    public Mono<String> bookTicket(@PathVariable Long flightId, @RequestBody @Valid BookingRequest req) {
        return service.bookTicket(flightId, req);
    }

    @GetMapping("/ticket/{pnr}")
    public Mono<Booking> getTicket(@PathVariable String pnr) {
        return service.getTicket(pnr);
    }

    @GetMapping("/booking/history/{email}")
    public Flux<Booking> getBookingHistory(@PathVariable String email) {
        return service.getBookingHistory(email);
    }

    @DeleteMapping("/booking/cancel/{pnr}")
    public Mono<String> cancelBooking(@PathVariable String pnr) {
        return service.cancelBooking(pnr);
    }

    @PutMapping("/booking/update/{pnr}")
    public Mono<Booking> updateBooking(@PathVariable String pnr, @RequestBody @Valid BookingRequest req) {
        return service.updateBooking(pnr, req);
    }
}
