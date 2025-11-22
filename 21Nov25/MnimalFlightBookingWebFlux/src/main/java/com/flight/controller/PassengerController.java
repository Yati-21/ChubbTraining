package com.flight.controller;

import com.flight.entity.Passenger;
import com.flight.service.PassengerService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public Flux<Passenger> getAll() {
        return passengerService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Passenger> getById(@PathVariable Integer id) {
        return passengerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Passenger> create(@RequestBody Passenger passenger) {
        return passengerService.save(passenger);
    }

    @PutMapping("/{id}")
    public Mono<Passenger> update(@PathVariable Integer id, @RequestBody Passenger passenger) {
        return passengerService.update(id, passenger);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Integer id) {
        return passengerService.deleteById(id);
    }

    @DeleteMapping
    public Mono<Void> deleteAll() {
        return passengerService.deleteAll();
    }

    @GetMapping("/booking/{bookingId}")
    public Flux<Passenger> findByBooking(@PathVariable Integer bookingId) {
        return passengerService.findByBookingId(bookingId);
    }
}
