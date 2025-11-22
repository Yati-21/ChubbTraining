package com.flight.service;

import com.flight.entity.Flight;
import com.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public Flux<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Mono<Flight> findById(Integer id) {
        return flightRepository.findById(id);
    }

    public Mono<Flight> save(Flight flight) {
        return flightRepository.save(flight);
    }

    public Mono<Flight> update(Integer id, Flight flight) {
        return flightRepository.findById(id)
                .flatMap(existing -> {
                    flight.setId(id);
                    return flightRepository.save(flight);
                });
    }

    public Mono<Void> deleteById(Integer id) {
        return flightRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return flightRepository.deleteAll();
    }

    public Flux<Flight> findByOriginAndDestination(String origin, String destination) {
        return flightRepository.findByOriginAndDestination(origin, destination);
    }

    public Flux<Flight> findByFlightNumberContaining(String key) {
        return flightRepository.findByFlightNumberContaining(key);
    }
}
