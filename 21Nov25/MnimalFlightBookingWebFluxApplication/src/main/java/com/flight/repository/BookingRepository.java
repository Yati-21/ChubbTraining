package com.flight.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.flight.entity.Booking;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingRepository extends R2dbcRepository<Booking, Integer> {

    Mono<Booking> findByPnr(String pnr);

    Flux<Booking> findByEmail(String email);
}
