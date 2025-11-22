package com.flight.controller;

import com.flight.entity.Flight;
import com.flight.service.FlightService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public Flux<Flight> getAllFlights(@RequestParam(required = false) String flightNumber) {
        return (flightNumber == null)
                ? flightService.findAll()
                : flightService.findByFlightNumberContaining(flightNumber);
    }

    @GetMapping("/{id}")
    public Mono<Flight> getById(@PathVariable Integer id) {
        return flightService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Flight> createFlight(@RequestBody Flight flight) {
        return flightService.save(flight);
    }

    @PutMapping("/{id}")
    public Mono<Flight> updateFlight(@PathVariable Integer id, @RequestBody Flight flight) {
        return flightService.update(id, flight);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteFlight(@PathVariable Integer id) {
        return flightService.deleteById(id);
    }

    @DeleteMapping
    public Mono<Void> deleteAll() {
        return flightService.deleteAll();
    }

    @GetMapping("/search")
    public Flux<Flight> search(@RequestParam String origin, @RequestParam String destination) {
        return flightService.findByOriginAndDestination(origin, destination);
    }
}
