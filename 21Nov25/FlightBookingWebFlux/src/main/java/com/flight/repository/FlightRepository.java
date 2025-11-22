package com.flight.repository;


import org.springframework.stereotype.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.AirportCode;
import com.flight.entity.Flight;

import reactor.core.publisher.Flux;

@Repository
public interface FlightRepository extends ReactiveCrudRepository<Flight, Long> {
    Flux<Flight> findByFromCityAndToCity(AirportCode fromCity, AirportCode toCity);
}
