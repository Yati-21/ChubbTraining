package com.flight.controller;

import com.flight.entity.Booking;
import com.flight.entity.Passenger;
import com.flight.service.BookingService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService service;

    @PostMapping("/{flightId}")
    public Mono<String> book(
            @PathVariable Integer flightId,
            @RequestBody BookingRequest request
    ) {
        return service.book(
                flightId,
                request.getBooking(),
                Flux.fromIterable(request.getPassengers())
        );
    }

    @GetMapping("/ticket/{pnr}")
    public Mono<Booking> ticket(@PathVariable String pnr) {
        return service.getByPnr(pnr);
    }

    @GetMapping("/history/{email}")
    public Flux<Booking> history(@PathVariable String email) {
        return service.historyByEmail(email);
    }

    @DeleteMapping("/cancel/{pnr}")
    public Mono<Void> cancel(@PathVariable String pnr) {
        return service.cancel(pnr);
    }
}
