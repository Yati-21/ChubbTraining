package com.flight.controller;

import com.flight.entity.Flight;
import com.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService service;

    @PostMapping("/inventory/add")
    public Mono<Integer> add(@RequestBody Flight f) {
        return service.add(f).map(Flight::getId);
    }

    @PostMapping("/search")
    public Flux<Flight> search(@RequestBody Flight req) {
        return service.search(req.getOrigin(), req.getDestination(), req.getDate());
    }
}
