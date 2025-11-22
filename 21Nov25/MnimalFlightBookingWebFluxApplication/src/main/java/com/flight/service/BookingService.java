package com.flight.service;

import org.springframework.stereotype.Service;
import com.flight.repository.*;
import com.flight.entity.*;
import com.flight.util.PnrGenerator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final PassengerRepository passengerRepo;
    private final FlightRepository flightRepo;

    public Mono<String> book(Integer flightId, Booking bookingInput, Flux<Passenger> passengers) {

        return flightRepo.findById(flightId)
                .flatMap(flight -> {
                    String pnr = PnrGenerator.generate();

                    Booking booking = new Booking(
                            null, flightId, pnr,
                            bookingInput.getEmail(),
                            bookingInput.getSeats(),
                            bookingInput.getMeal(),
                            bookingInput.getSeatNumbers()
                    );

                    return bookingRepo.save(booking)
                            .flatMap(savedBooking ->
                                passengers.flatMap(p -> {
                                    p.setBookingId(savedBooking.getId());
                                    return passengerRepo.save(p);
                                }).then(Mono.just(pnr))
                            );
                });
    }

    public Mono<Booking> getByPnr(String pnr) {
        return bookingRepo.findByPnr(pnr);
    }

    public Flux<Booking> historyByEmail(String email) {
        return bookingRepo.findByEmail(email);
    }

    public Mono<Void> cancel(String pnr) {
        return bookingRepo.findByPnr(pnr)
                .flatMap(b -> bookingRepo.deleteById(b.getId()));
    }
}
