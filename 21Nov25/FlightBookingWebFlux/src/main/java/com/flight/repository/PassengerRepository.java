package com.flight.repository;


import org.springframework.stereotype.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Passenger;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Repository
public interface PassengerRepository extends ReactiveCrudRepository<Passenger, Long> {

    Mono<Boolean> existsBySeatNumberAndFlightId(String seatNumber, Long flightId);

    Flux<Passenger> findByBookingId(Long bookingId);
}
