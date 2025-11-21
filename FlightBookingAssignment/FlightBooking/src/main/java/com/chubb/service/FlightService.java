package com.chubb.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.chubb.entity.AirportCode;
import com.chubb.entity.Booking;
import com.chubb.entity.Flight;
import com.chubb.entity.Passenger;
import com.chubb.exception.BusinessException;
import com.chubb.exception.NotFoundException;
import com.chubb.exception.SeatUnavailableException;
import com.chubb.repository.BookingRepository;
import com.chubb.repository.FlightRepository;
import com.chubb.repository.PassengerRepository;
import com.chubb.request.BookingRequest;
import com.chubb.request.PassengerRequest;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FlightService implements FlightServiceInterface 
{
	private static final String PNR_PREFIX = "PNR";
	private static final int PNR_MIN = 10000;
	private static final int PNR_MAX = 99999;
	private static final long CANCELLATION_LIMIT_HOURS = 24;


	//construction injection instead of autowired - sonarqube suggestion
	private final FlightRepository flightRepo;
    private final BookingRepository bookingRepo;
    private final PassengerRepository passengerRepo;

    public FlightService(FlightRepository flightRepo, BookingRepository bookingRepo,PassengerRepository passengerRepo) 
    {
        this.flightRepo =flightRepo;
        this.bookingRepo =bookingRepo;
        this.passengerRepo =passengerRepo;
    }

    @Override
    public Flight addFlight(Flight flight) 
    {
    	
        flight.setAvailableSeats(flight.getTotalSeats());
        return flightRepo.save(flight);
    }

    @Override
    public List<Flight> searchFlights(String from,String to,LocalDate journeyDate) 
    {
        List<Flight> flights=flightRepo.findByFromCityAndToCity(
                AirportCode.fromString(from),
                AirportCode.fromString(to)
        );
        return flights.stream().filter(f->f.getDepartureTime().toLocalDate().equals(journeyDate)).toList();
    }


    @Override
    @Transactional
    public String bookTicket(Long flightId,BookingRequest req) 
    {

        Flight flight =flightRepo.findById(flightId).orElse(null);
        if (flight ==null) {
            throw new NotFoundException("Flight not found");
        }

        //validate seats vs passengers
        if (req.getPassengers() ==null || req.getPassengers().size() !=req.getSeats()) 
        {
        	throw new BusinessException("Number of passengers must match seats booked");
        }
        
        //check passenger duplicates (name+age+gender)
        validatePassengerDuplicates(req.getPassengers());
        //check seat availability
        validateSeatAvailability(flightId,req.getPassengers());

        if (flight.getAvailableSeats()<req.getSeats()) {
        	throw new SeatUnavailableException("Not enough seats available");
        }

        //reduce available seats
        flight.setAvailableSeats(flight.getAvailableSeats() -req.getSeats());
        flightRepo.save(flight);

        //create booking
        Booking booking=new Booking();
        booking.setPnr(generateUniquePnr());
        booking.setFlight(flight);
        booking.setName(req.getName());
        booking.setEmail(req.getEmail());
        booking.setSeatsBooked(req.getSeats());
        booking.setMealType(req.getMealType());

        Booking savedBooking = bookingRepo.save(booking);
        
        //save all passengers
        for (PassengerRequest passengerRequest : req.getPassengers()) {
            Passenger passenger=new Passenger();
            passenger.setName(passengerRequest.getName());
            passenger.setGender(passengerRequest.getGender());
            passenger.setAge(passengerRequest.getAge());
            passenger.setSeatNumber(passengerRequest.getSeatNumber());
            passenger.setBooking(savedBooking);
            passengerRepo.save(passenger);
        }

        return savedBooking.getPnr();
    }

    @Override
    public Booking getTicket(String pnr) 
    {
        Booking booking =bookingRepo.findByPnr(pnr);
        if (booking== null) 
        {
        	throw new NotFoundException("PNR not found");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingHistory(String email) 
    {
        return bookingRepo.findByEmail(email);
    }
    
    
    @Override
    @Transactional
    public String cancelBooking(String pnr) 
    {
        Booking booking = bookingRepo.findByPnr(pnr);
        if (booking ==null) 
        {
            throw new NotFoundException("Invalid PNR");
        }

        Flight flight =booking.getFlight();
        LocalDateTime now= LocalDateTime.now();
        LocalDateTime dep= flight.getDepartureTime();
        
        long hoursDiff=Duration.between(now,dep).toHours();
        
        //check 24 hours rule
        if (hoursDiff <CANCELLATION_LIMIT_HOURS) 
        {
        	throw new BusinessException("Cannot cancel ticket within 24 hours of departure");
        }

        //restore seats
        flight.setAvailableSeats(flight.getAvailableSeats()+booking.getSeatsBooked());
        flightRepo.save(flight);
        bookingRepo.delete(booking);
        return "Booking cancelled successfully: "+pnr;
    }
    
    
    
    @Override
    @Transactional
    public Booking updateBooking(String pnr, BookingRequest req) 
    {
        Booking booking =bookingRepo.findByPnr(pnr);
        if (booking ==null) 
        {
        	throw new NotFoundException("PNR not found");
        }

        //check 24 hours rule (same as cancel)
        LocalDateTime now =LocalDateTime.now();
        LocalDateTime dep =booking.getFlight().getDepartureTime();
        long hoursDiff =Duration.between(now,dep).toHours();

        if (hoursDiff<CANCELLATION_LIMIT_HOURS) {
            throw new BusinessException("Cannot update booking within 24 hours of departure");
        }

        Flight flight=booking.getFlight();
        
        //adjust available seats on flight
        int oldSeats=booking.getSeatsBooked();
        int newSeats=req.getSeats();
        
        int diff=newSeats - oldSeats; // +ve if more seats requested, -ve if reduced

        if (diff>0 && flight.getAvailableSeats()<diff) {
        	throw new SeatUnavailableException("Not enough seats available");
        }

        //apply seat difference
        flight.setAvailableSeats(flight.getAvailableSeats() - diff);
        flightRepo.save(flight);

        //update booking fields
        booking.setName(req.getName());
        booking.setEmail(req.getEmail());
        booking.setSeatsBooked(newSeats);
        booking.setMealType(req.getMealType());

        //remove old passengers if any
        if (booking.getPassengers()!=null && !booking.getPassengers().isEmpty()) 
        {
            passengerRepo.deleteAll(booking.getPassengers());
        }
        
        validateSeatAvailability(flight.getId(), req.getPassengers());
        //check duplicate passengers (name+age+gender)
        validatePassengerDuplicates(req.getPassengers());
        
        //save new passengers
        if (req.getPassengers() != null) 
        {
            for (PassengerRequest passengerRequest : req.getPassengers()) 
            {
                Passenger passenger = new Passenger();
                passenger.setName(passengerRequest.getName());
                passenger.setGender(passengerRequest.getGender());
                passenger.setAge(passengerRequest.getAge());
                passenger.setSeatNumber(passengerRequest.getSeatNumber());
                passenger.setBooking(booking);
                passengerRepo.save(passenger);
            }
        }
        return bookingRepo.save(booking);
    }

    //unique PNR generation to prevent collison
    private String generateUniquePnr() 
    {
        String pnr;
        do 
        {
        	//generate random number between PNR_MIN and PNR_MAX
        	int number=ThreadLocalRandom.current().nextInt(PNR_MIN, PNR_MAX);
        	pnr=PNR_PREFIX +number;

        }
        while (bookingRepo.findByPnr(pnr) != null);
        return pnr;
    }
    
    private void validateSeatAvailability(Long flightId, List<PassengerRequest> passengers) 
    {
        Set<String> seats = new HashSet<>();
        for(PassengerRequest p : passengers) 
        {
        	//check duplicate seat in request
            if(!seats.add(p.getSeatNumber())) 
            {
                throw new SeatUnavailableException("Duplicate seat in request: "+p.getSeatNumber());
            }
            //check if seat already booked in DB
            if(passengerRepo.existsBySeatNumberAndBooking_Flight_Id(p.getSeatNumber(),flightId)) 
            {
                throw new SeatUnavailableException("Seat already booked: "+p.getSeatNumber());
            }
        }
    }
    
    private void validatePassengerDuplicates(List<PassengerRequest> passengers) 
    {
        Set<String> set=new HashSet<>();
        for (PassengerRequest p : passengers) 
        {
            String key=p.getName()+"-" + p.getAge()+"-"+p.getGender();
            if (!set.add(key)) {
                throw new BusinessException("Duplicate passenger detected: "+p.getName());
            }
        }
    }

    

}