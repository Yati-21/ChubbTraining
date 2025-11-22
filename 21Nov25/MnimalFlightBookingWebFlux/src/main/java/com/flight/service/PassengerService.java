package com.flight.service;

import com.flight.entity.Passenger;
import com.flight.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public Flux<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    public Mono<Passenger> findById(Integer id) {
        return passengerRepository.findById(id);
    }

    public Mono<Passenger> save(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Mono<Passenger> update(Integer id, Passenger passenger) {
        return passengerRepository.findById(id)
                .flatMap(existing -> {
                    passenger.setId(id);
                    return passengerRepository.save(passenger);
                });
    }

    public Mono<Void> deleteById(Integer id) {
        return passengerRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return passengerRepository.deleteAll();
    }

    public Flux<Passenger> findByBookingId(Integer bookingId) {
        return passengerRepository.findByBookingId(bookingId);
    }
}
