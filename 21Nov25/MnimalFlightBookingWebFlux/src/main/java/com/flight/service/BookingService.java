package com.flight.service;

import com.flight.dto.BookingResponse;
import com.flight.entity.Booking;
import com.flight.entity.Flight;
import com.flight.entity.Passenger;
import com.flight.repository.BookingRepository;
import com.flight.repository.FlightRepository;
import com.flight.repository.PassengerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;

    public Flux<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Mono<Booking> findById(Integer id) {
        return bookingRepository.findById(id);
    }

    public Mono<Booking> save(Booking booking) {
        booking.setPnr(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        booking.setBookingDate(LocalDate.now().toString());
        return bookingRepository.save(booking);
    }

    public Mono<Booking> update(Integer id, Booking booking) {
        return bookingRepository.findById(id)
                .flatMap(existing -> {
                    booking.setId(id);
                    return bookingRepository.save(booking);
                });
    }

    public Mono<Void> deleteById(Integer id) {
        return bookingRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return bookingRepository.deleteAll();
    }

    public Mono<Booking> findByPnr(String pnr) {
        return bookingRepository.findByPnr(pnr);
    }

    public Flux<Passenger> findPassengers(Integer bookingId) {
        return passengerRepository.findByBookingId(bookingId);
    }

    public Mono<BookingResponse> getBookingDetails(String pnr) {
        return bookingRepository.findByPnr(pnr)
                .flatMap(booking ->
                        passengerRepository.findByBookingId(booking.getId()).collectList()
                                .flatMap(passengers ->
                                        flightRepository.findById(booking.getFlightId())
                                                .defaultIfEmpty(new Flight())
                                                .map(flight -> new BookingResponse(booking, passengers, flight))
                                )
                );
    }
}
