package com.flight.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.flight.entity.Flight;
import reactor.core.publisher.Flux;

public interface FlightRepository extends R2dbcRepository<Flight, Integer> {

    Flux<Flight> findByOriginAndDestinationAndDate(String origin, String destination, String date);
}
