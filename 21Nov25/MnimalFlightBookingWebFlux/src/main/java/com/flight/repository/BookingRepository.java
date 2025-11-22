package com.flight.repository;

import com.flight.entity.Booking;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface BookingRepository extends R2dbcRepository<Booking, Integer> {
    Mono<Booking> findByPnr(String pnr);
}
