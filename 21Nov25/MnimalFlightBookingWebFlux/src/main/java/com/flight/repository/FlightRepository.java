package com.flight.repository;

import com.flight.entity.Flight;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface FlightRepository extends R2dbcRepository<Flight, Integer> {
    Flux<Flight> findByOriginAndDestination(String origin, String destination);
    Flux<Flight> findByFlightNumberContaining(String flightNumber);
}
