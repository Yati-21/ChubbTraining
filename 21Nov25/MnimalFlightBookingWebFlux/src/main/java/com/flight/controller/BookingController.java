package com.flight.controller;

import com.flight.dto.BookingResponse;
import com.flight.entity.Booking;
import com.flight.entity.Passenger;
import com.flight.service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public Flux<Booking> getAllBookings() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Booking> getById(@PathVariable Integer id) {
        return bookingService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Booking> createBooking(@RequestBody Booking booking) {
        return bookingService.save(booking);
    }

    @PutMapping("/{id}")
    public Mono<Booking> updateBooking(@PathVariable Integer id, @RequestBody Booking booking) {
        return bookingService.update(id, booking);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBooking(@PathVariable Integer id) {
        return bookingService.deleteById(id);
    }

    @DeleteMapping
    public Mono<Void> deleteAll() {
        return bookingService.deleteAll();
    }

    @GetMapping("/pnr/{pnr}")
    public Mono<Booking> getByPnr(@PathVariable String pnr) {
        return bookingService.findByPnr(pnr);
    }

    @GetMapping("/pnr/{pnr}/details")
    public Mono<BookingResponse> getBookingDetails(@PathVariable String pnr) {
        return bookingService.getBookingDetails(pnr);
    }
}
