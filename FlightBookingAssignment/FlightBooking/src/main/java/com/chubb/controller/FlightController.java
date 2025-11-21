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
public class FlightController 
{

	//using construction injection instead of autowired- sonarqube suggestion
    private FlightService service;
    public FlightController(FlightService service) {
        this.service = service;
    }
    
    @PostMapping("/airline/inventory/add")
    public Flight addFlight(@RequestBody @Valid Flight flight) 
    {
        log.debug("Adding flight: "+flight.getFlightNumber());
        return service.addFlight(flight);
    }

    
    @PostMapping("/search")
    public List<Flight> search(@Valid @RequestBody FlightSearchRequest request) 
    {
        return service.searchFlights(request.getFrom(), request.getTo(), request.getJourneyDate());
    }


    @PostMapping("/booking/{flightId}")
    public String bookTicket(@PathVariable Long flightId,@RequestBody @Valid BookingRequest req) 
    {
        return service.bookTicket(flightId,req);
    }

    @GetMapping("/ticket/{pnr}")
    public Booking getTicket(@PathVariable String pnr) 
    {
        return service.getTicket(pnr);
    }
    
    
    @GetMapping("/booking/history/{email}")
    public List<Booking> getBookingHistory(@PathVariable String email) 
    {
        return service.getBookingHistory(email);
    }
    
    @DeleteMapping("/booking/cancel/{pnr}")
    public String cancelBooking(@PathVariable String pnr) 
    {
        return service.cancelBooking(pnr);
    }
    
    @PutMapping("/booking/update/{pnr}")
    public Booking updateBooking(@PathVariable String pnr,@RequestBody @Valid BookingRequest req) 
    {
        return service.updateBooking(pnr, req);
    }

    
}