package com.flight.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Booking;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Repository
public interface BookingRepository extends ReactiveCrudRepository<Booking, Long> {
    Mono<Booking> findByPnr(String pnr);
    Flux<Booking> findByEmail(String email);
    Mono<Boolean> existsByPnr(String pnr);
}
