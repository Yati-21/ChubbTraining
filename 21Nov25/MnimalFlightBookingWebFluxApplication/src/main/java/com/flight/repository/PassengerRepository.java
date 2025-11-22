package com.flight.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.flight.entity.Passenger;
import reactor.core.publisher.Flux;

public interface PassengerRepository extends R2dbcRepository<Passenger, Integer> {

    Flux<Passenger> findByBookingId(Integer bookingId);
}
