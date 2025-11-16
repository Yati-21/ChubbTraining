package com.chubb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.entity.Booking;
import com.chubb.entity.Flight;
import com.chubb.request.BookingRequest;
import com.chubb.request.FlightSearchRequest;
import com.chubb.service.FlightService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/flight")
public class FlightController {

    @Autowired
    private FlightService service;

    // POST /api/v1.0/flight/airline/inventory/add
    @PostMapping("/airline/inventory/add")
    public Flight addFlight(@RequestBody @Valid Flight flight) {
        log.debug("Adding flight: " + flight.getFlightNumber());
        return service.addFlight(flight);
    }

    
    // POST /api/v1.0/flight/search
    @PostMapping("/search")
    public List<Flight> search(@RequestBody FlightSearchRequest request) {
    	return service.searchFlights(request.from, request.to, request.journeyDate);

    }


    
    // POST /api/v1.0/flight/booking/{flightId}
    @PostMapping("/booking/{flightId}")
    public String bookTicket(@PathVariable Long flightId,@RequestBody @Valid BookingRequest req) {
        return service.bookTicket(flightId, req);
    }

    // GET /api/v1.0/flight/ticket/{pnr}
    @GetMapping("/ticket/{pnr}")
    public Booking getTicket(@PathVariable String pnr) {
        return service.getTicket(pnr);
    }
    
    
    // GET /api/v1.0/flight/booking/history/{email}
    @GetMapping("/booking/history/{email}")
    public List<Booking> getBookingHistory(@PathVariable String email) {
        return service.getBookingHistory(email);
    }
    
    // DELETE /api/v1.0/flight/booking/cancel/{pnr}
    @DeleteMapping("/booking/cancel/{pnr}")
    public String cancelBooking(@PathVariable String pnr) {
        return service.cancelBooking(pnr);
    }
    
    // PUT /api/v1.0/flight/booking/update/{pnr}
    @PutMapping("/booking/update/{pnr}")
    public Booking updateBooking(@PathVariable String pnr, @RequestBody @Valid BookingRequest req) {
        return service.updateBooking(pnr, req);
    }

    
}