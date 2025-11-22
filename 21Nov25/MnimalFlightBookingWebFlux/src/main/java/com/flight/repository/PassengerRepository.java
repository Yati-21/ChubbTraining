package com.flight.repository;

import com.flight.entity.Passenger;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PassengerRepository extends R2dbcRepository<Passenger, Integer> {
    Flux<Passenger> findByBookingId(Integer bookingId);
}
