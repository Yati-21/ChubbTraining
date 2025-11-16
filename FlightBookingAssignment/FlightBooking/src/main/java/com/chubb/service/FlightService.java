package com.chubb.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.entity.AirportCode;
import com.chubb.entity.Booking;
import com.chubb.entity.Flight;
import com.chubb.entity.Passenger;
import com.chubb.repository.BookingRepository;
import com.chubb.repository.FlightRepository;
import com.chubb.repository.PassengerRepository;
import com.chubb.request.BookingRequest;
import com.chubb.request.PassengerRequest;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FlightService implements FlightServiceInterface {

    @Autowired
    private FlightRepository flightRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PassengerRepository passengerRepo;

    
    public Flight addFlight(Flight flight) 
    {
        flight.setAvailableSeats(flight.getTotalSeats());
        return flightRepo.save(flight);
    }


    public List<Flight> searchFlights(String from, String to, LocalDate journeyDate) {

        List<Flight> flights = flightRepo.findByFromCityAndToCity(
                AirportCode.valueOf(from),
                AirportCode.valueOf(to)
        );

        if (journeyDate == null) {
            return flights;
        }

        return flights.stream()
                .filter(f -> f.getDepartureTime() != null &&
                        f.getDepartureTime().toLocalDate().equals(journeyDate))
                .toList();
    }


    @Transactional
    public String bookTicket(Long flightId, BookingRequest req) {

        Flight flight = flightRepo.findById(flightId).orElse(null);
        
        if (flight == null) {
            throw new RuntimeException("Flight not found");
        }
        if (req.passengers == null || req.passengers.size() != req.seats) {
            throw new RuntimeException("Number of passengers must match seats booked");
        }
        if (flight.getAvailableSeats() < req.seats) {
            throw new RuntimeException("Not enough seats available");
        }


        // 3) reduce available seats
        flight.setAvailableSeats(flight.getAvailableSeats() - req.seats);
        flightRepo.save(flight);

        // 4) create booking
        Booking booking = new Booking();
        booking.setPnr(generateUniquePnr());
        booking.setFlight(flight);
        booking.setName(req.name);
        booking.setEmail(req.email);
        booking.setSeatsBooked(req.seats);
        booking.setMealType(req.mealType);

        Booking savedBooking = bookingRepo.save(booking);

        
        // 5) save all passengers
        for (PassengerRequest passengerRequest : req.passengers) {
            Passenger passenger = new Passenger();
            passenger.setName(passengerRequest.name);
            passenger.setGender(passengerRequest.gender);
            passenger.setAge(passengerRequest.age);
            passenger.setSeatNumber(passengerRequest.seatNumber);
            passenger.setBooking(savedBooking);

            passengerRepo.save(passenger);
        }

        return savedBooking.getPnr();
    }

    public Booking getTicket(String pnr) {
        Booking booking = bookingRepo.findByPnr(pnr);
        if (booking == null) {
            throw new RuntimeException("Invalid PNR");
        }
        return booking;
    }

    
    public List<Booking> getBookingHistory(String email) {
        return bookingRepo.findByEmail(email);
    }
    
    
    @Override
    @Transactional
    public String cancelBooking(String pnr) {

        Booking booking = bookingRepo.findByPnr(pnr);
        if (booking == null) {
            throw new RuntimeException("Invalid PNR");
        }

        Flight flight = booking.getFlight();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dep = flight.getDepartureTime();

        long hoursDiff = Duration.between(now, dep).toHours();
        if (hoursDiff < 24) {
            throw new RuntimeException("Cannot cancel ticket within 24 hours of departure");
        }

        // restore seats
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatsBooked());
        flightRepo.save(flight);
        bookingRepo.delete(booking);
        return "Booking cancelled successfully: " + pnr;
    }
    
    
    
    @Override
    @Transactional
    public Booking updateBooking(String pnr, BookingRequest req) {

        Booking booking = bookingRepo.findByPnr(pnr);
        if (booking == null) {
            throw new RuntimeException("Invalid PNR");
        }

        // Check 24 hours rule (same as cancel)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dep = booking.getFlight().getDepartureTime();
        long hoursDiff = Duration.between(now, dep).toHours();

        if (hoursDiff < 24) {
            throw new RuntimeException("Cannot update booking within 24 hours of departure");
        }

        Flight flight = booking.getFlight();

        // ----- NEW: adjust available seats on flight -----
        int oldSeats = booking.getSeatsBooked();
        int newSeats = req.seats;
        int diff = newSeats - oldSeats; // +ve if more seats requested, -ve if reduced

        if (diff > 0 && flight.getAvailableSeats() < diff) {
            throw new RuntimeException("Not enough seats available for update");
        }

        // apply seat difference
        flight.setAvailableSeats(flight.getAvailableSeats() - diff);
        flightRepo.save(flight);
        // ---------------------------------------------

        // update booking fields
        booking.setName(req.name);
        booking.setEmail(req.email);
        booking.setSeatsBooked(newSeats);
        booking.setMealType(req.mealType);

        // remove old passengers if any
        if (booking.getPassengers() != null && !booking.getPassengers().isEmpty()) {
            passengerRepo.deleteAll(booking.getPassengers());
        }

        // save new passengers
        if (req.passengers != null) {
            for (PassengerRequest passengerRequest : req.passengers) {
                Passenger passenger = new Passenger();
                passenger.setName(passengerRequest.name);
                passenger.setGender(passengerRequest.gender);
                passenger.setAge(passengerRequest.age);
                passenger.setSeatNumber(passengerRequest.seatNumber);
                passenger.setBooking(booking);
                passengerRepo.save(passenger);
            }
        }

        return bookingRepo.save(booking);
    }

    //unique PNR generation to prevent collison
    private String generateUniquePnr() 
    {
        Random random = new Random();
        String pnr;
        do {
            int number = 10000 + random.nextInt(90000); // always 5 digits
            pnr = "PNR" + number;
        } while (bookingRepo.findByPnr(pnr) != null);
        return pnr;
    }

}