package com.flight.service;

import org.springframework.stereotype.Service;
import com.flight.repository.FlightRepository;
import com.flight.entity.Flight;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository repo;

    public Mono<Flight> add(Flight f) {
        return repo.save(f);
    }

    public Flux<Flight> search(String origin, String destination, String date) {
        return repo.findByOriginAndDestinationAndDate(origin, destination, date);
    }

    public Mono<Flight> findById(Integer id) {
        return repo.findById(id);
    }
}
