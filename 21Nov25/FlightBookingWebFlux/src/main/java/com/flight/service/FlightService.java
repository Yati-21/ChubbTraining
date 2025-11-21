package com.flight.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight.entity.AirportCode;
import com.flight.entity.Booking;
import com.flight.entity.Flight;
import com.flight.entity.Passenger;
import com.flight.exception.BusinessException;
import com.flight.exception.NotFoundException;
import com.flight.exception.SeatUnavailableException;
import com.flight.repository.BookingRepository;
import com.flight.repository.FlightRepository;
import com.flight.repository.PassengerRepository;
import com.flight.request.BookingRequest;
import com.flight.request.PassengerRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightService implements FlightServiceInterface {

    private static final String PNR_PREFIX = "PNR";
    private static final int PNR_MIN = 10000;
    private static final int PNR_MAX = 99999;
    private static final long CANCELLATION_LIMIT_HOURS = 24;

    private final FlightRepository flightRepo;
    private final BookingRepository bookingRepo;
    private final PassengerRepository passengerRepo;

    @Override
    public Mono<Flight> addFlight(Flight flight) {
        flight.setAvailableSeats(flight.getTotalSeats());
        return flightRepo.save(flight);
    }

    @Override
    public Flux<Flight> searchFlights(String from, String to, LocalDate journeyDate) {
        AirportCode fromCode = AirportCode.fromString(from);
        AirportCode toCode = AirportCode.fromString(to);

        return flightRepo.findByFromCityAndToCity(fromCode, toCode)
                .filter(f -> f.getDepartureTime().toLocalDate().equals(journeyDate));
    }

    @Override
    @Transactional
    public Mono<String> bookTicket(Long flightId, BookingRequest req) {
        return flightRepo.findById(flightId)
                .switchIfEmpty(Mono.error(new NotFoundException("Flight not found")))
                .flatMap(flight -> {
                    if (req.getPassengers() == null || req.getPassengers().size() != req.getSeats()) {
                        return Mono.error(new BusinessException("Number of passengers must match seats booked"));
                    }

                    return validatePassengerDuplicatesReactive(req.getPassengers())
                            .then(validateSeatAvailabilityReactive(flightId, req.getPassengers()))
                            .then(Mono.defer(() -> {
                                if (flight.getAvailableSeats() < req.getSeats()) {
                                    return Mono.error(new SeatUnavailableException("Not enough seats available"));
                                }
                                flight.setAvailableSeats(flight.getAvailableSeats() - req.getSeats());
                                return flightRepo.save(flight)
                                        .flatMap(savedFlight -> generateUniquePnrReactive()
                                                .flatMap(pnr -> {
                                                    Booking booking = new Booking();
                                                    booking.setPnr(pnr);
                                                    booking.setFlightId(savedFlight.getId());
                                                    booking.setName(req.getName());
                                                    booking.setEmail(req.getEmail());
                                                    booking.setSeatsBooked(req.getSeats());
                                                    booking.setMealType(req.getMealType());
                                                    return bookingRepo.save(booking)
                                                            .flatMap(savedBooking -> Flux.fromIterable(req.getPassengers())
                                                                    .flatMap(pr -> {
                                                                        Passenger p = new Passenger();
                                                                        p.setName(pr.getName());
                                                                        p.setGender(pr.getGender());
                                                                        p.setAge(pr.getAge());
                                                                        p.setSeatNumber(pr.getSeatNumber());
                                                                        p.setBookingId(savedBooking.getId());
                                                                        p.setFlightId(savedFlight.getId());
                                                                        return passengerRepo.save(p);
                                                                    })
                                                                    .then(Mono.just(savedBooking.getPnr())));
                                                }));
                            }));
                });
    }

    @Override
    public Mono<Booking> getTicket(String pnr) {
        return bookingRepo.findByPnr(pnr)
                .switchIfEmpty(Mono.error(new NotFoundException("PNR not found")));
    }

    @Override
    public Flux<Booking> getBookingHistory(String email) {
        return bookingRepo.findByEmail(email);
    }

    @Override
    @Transactional
    public Mono<String> cancelBooking(String pnr) {
        return bookingRepo.findByPnr(pnr)
                .switchIfEmpty(Mono.error(new NotFoundException("Invalid PNR")))
                .flatMap(booking -> flightRepo.findById(booking.getFlightId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Flight not found for booking")))
                        .flatMap(flight -> {
                            LocalDateTime now = LocalDateTime.now();
                            LocalDateTime dep = flight.getDepartureTime();
                            long hoursDiff = Duration.between(now, dep).toHours();
                            if (hoursDiff < CANCELLATION_LIMIT_HOURS) {
                                return Mono.error(new BusinessException("Cannot cancel ticket within 24 hours of departure"));
                            }
                            flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatsBooked());
                            return flightRepo.save(flight)
                                    .then(bookingRepo.delete(booking))
                                    .then(Mono.just("Booking cancelled successfully: " + pnr));
                        }));
    }

    @Override
    @Transactional
    public Mono<Booking> updateBooking(String pnr, BookingRequest req) {
        return bookingRepo.findByPnr(pnr)
                .switchIfEmpty(Mono.error(new NotFoundException("PNR not found")))
                .flatMap(booking -> flightRepo.findById(booking.getFlightId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Flight not found")))
                        .flatMap(flight -> {
                            LocalDateTime now = LocalDateTime.now();
                            LocalDateTime dep = flight.getDepartureTime();
                            long hoursDiff = Duration.between(now, dep).toHours();
                            if (hoursDiff < CANCELLATION_LIMIT_HOURS) {
                                return Mono.error(new BusinessException("Cannot update booking within 24 hours of departure"));
                            }

                            if (req.getPassengers() == null || req.getPassengers().size() != req.getSeats()) {
                                return Mono.error(new BusinessException("Number of passengers must match seats booked"));
                            }

                            int oldSeats = booking.getSeatsBooked();
                            int newSeats = req.getSeats();
                            int diff = newSeats - oldSeats;
                            if (diff > 0 && flight.getAvailableSeats() < diff) {
                                return Mono.error(new SeatUnavailableException("Not enough seats available"));
                            }

                            flight.setAvailableSeats(flight.getAvailableSeats() - diff);
                            return flightRepo.save(flight)
                                    .then(passengerRepo.findByBookingId(booking.getId()).collectList()
                                            .flatMap(existingPassengers -> {
                                                if (!existingPassengers.isEmpty()) {
                                                    return passengerRepo.deleteAll(existingPassengers).then(Mono.empty());
                                                }
                                                return Mono.empty();
                                            })
                                            .then(validateSeatAvailabilityReactive(flight.getId(), req.getPassengers()))
                                            .then(validatePassengerDuplicatesReactive(req.getPassengers()))
                                            .then(Mono.defer(() -> {
                                                booking.setName(req.getName());
                                                booking.setEmail(req.getEmail());
                                                booking.setSeatsBooked(newSeats);
                                                booking.setMealType(req.getMealType());
                                                return bookingRepo.save(booking)
                                                        .flatMap(savedBooking -> Flux.fromIterable(req.getPassengers())
                                                                .flatMap(pr -> {
                                                                    Passenger p = new Passenger();
                                                                    p.setName(pr.getName());
                                                                    p.setGender(pr.getGender());
                                                                    p.setAge(pr.getAge());
                                                                    p.setSeatNumber(pr.getSeatNumber());
                                                                    p.setBookingId(savedBooking.getId());
                                                                    p.setFlightId(flight.getId());
                                                                    return passengerRepo.save(p);
                                                                }).then(Mono.just(savedBooking)));
                                            })));
                        }));
    }

    // reactive unique PNR generation
    private Mono<String> generateUniquePnrReactive() {
        return Mono.defer(() -> {
            int number = ThreadLocalRandom.current().nextInt(PNR_MIN, PNR_MAX);
            String pnr = PNR_PREFIX + number;
            return bookingRepo.existsByPnr(pnr)
                    .flatMap(exists -> exists ? generateUniquePnrReactive() : Mono.just(pnr));
        });
    }

    private Mono<Void> validateSeatAvailabilityReactive(Long flightId, List<PassengerRequest> passengers) {
        Set<String> seats = new HashSet<>();
        return Flux.fromIterable(passengers)
                .flatMap(p -> {
                    if (!seats.add(p.getSeatNumber())) {
                        return Mono.error(new SeatUnavailableException("Duplicate seat in request: " + p.getSeatNumber()));
                    }
                    return passengerRepo.existsBySeatNumberAndFlightId(p.getSeatNumber(), flightId)
                            .flatMap(exists -> exists ? Mono.<Void>error(new SeatUnavailableException("Seat already booked: " + p.getSeatNumber())) : Mono.empty());
                })
                .then();
    }

    private Mono<Void> validatePassengerDuplicatesReactive(List<PassengerRequest> passengers) {
        Set<String> set = new HashSet<>();
        for (PassengerRequest p : passengers) {
            String key = p.getName() + "-" + p.getAge() + "-" + p.getGender();
            if (!set.add(key)) {
                return Mono.error(new BusinessException("Duplicate passenger detected: " + p.getName()));
            }
        }
        return Mono.empty();
    }
}
