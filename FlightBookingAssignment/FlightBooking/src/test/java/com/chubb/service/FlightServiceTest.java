package com.chubb.service;
import static org.mockito.Mockito.never;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.entity.AirportCode;
import com.chubb.entity.Booking;
import com.chubb.entity.Flight;
import com.chubb.entity.Passenger;
import com.chubb.repository.BookingRepository;
import com.chubb.repository.FlightRepository;
import com.chubb.repository.PassengerRepository;
import com.chubb.request.BookingRequest;
import com.chubb.request.PassengerRequest;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepo;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private PassengerRepository passengerRepo;

    @InjectMocks
    private FlightService service;

    @Test
    void testSearchFlights() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFromCity(AirportCode.DEL);
        flight.setToCity(AirportCode.BOM);

        when(flightRepo.findByFromCityAndToCity(AirportCode.DEL, AirportCode.BOM))
                .thenReturn(List.of(flight));

        LocalDate journeyDate = LocalDate.of(2025, 12, 20);
        flight.setDepartureTime(journeyDate.atTime(9, 0));  //2025-12-20T09:00 - departureTime must match journeyDate

        List<Flight> result = service.searchFlights("DEL", "BOM", journeyDate);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testBookTicket() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(10);

        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

        BookingRequest req = new BookingRequest();
        req.name = "Ravi";
        req.email = "ravi@gmail.com";
        req.seats = 1;
        req.mealType = "VEG";

        PassengerRequest p1 = new PassengerRequest();
        p1.name = "Ravi";
        p1.gender = "Male";
        p1.age = 28;
        p1.seatNumber = "12A";

        req.passengers = List.of(p1);

        Booking booking = new Booking();
        booking.setPnr("PNR99999");
        booking.setMealType("VEG");

        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        String pnr = service.bookTicket(1L, req);

        assertEquals("PNR99999", pnr);
    }

    @Test
    void testBookTicketPassengerCountMismatchThrows() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(10);

        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

        BookingRequest req = new BookingRequest();
        req.name = "Ravi";
        req.email = "ravi@gmail.com";
        req.seats = 2;              // 2 seats
        req.mealType = "VEG";

        PassengerRequest p1 = new PassengerRequest();
        p1.name = "Ravi";
        p1.gender = "Male";
        p1.age = 28;
        p1.seatNumber = "12A";

        req.passengers = List.of(p1);   // but only 1 passenger

        assertThrows(RuntimeException.class, () -> service.bookTicket(1L, req));

        // seats should NOT be changed in this case
        assertEquals(10, flight.getAvailableSeats());
        verify(bookingRepo, never()).save(any(Booking.class));
    }
    
    @Test
    void testCancelBookingSuccess() {

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(50);
        flight.setDepartureTime(LocalDateTime.now().plusDays(2)); // > 24 hrs

        Booking booking = new Booking();
        booking.setPnr("PNR111");
        booking.setSeatsBooked(2);
        booking.setFlight(flight);

        when(bookingRepo.findByPnr("PNR111")).thenReturn(booking);

        String msg = service.cancelBooking("PNR111");

        assertTrue(msg.contains("successfully"));
        verify(bookingRepo, times(1)).delete(booking);
        verify(flightRepo, times(1)).save(flight);
    }
    
    
    @Test
    void testCancelBookingTooLate() {

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(50);
        flight.setDepartureTime(LocalDateTime.now().plusHours(10)); // < 24 hrs

        Booking booking = new Booking();
        booking.setPnr("PNR222");
        booking.setSeatsBooked(1);
        booking.setFlight(flight);

        when(bookingRepo.findByPnr("PNR222")).thenReturn(booking);

        assertThrows(RuntimeException.class, () -> {
            service.cancelBooking("PNR222");
        });
    }

    @Test
    void testUpdateBooking() {

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDepartureTime(LocalDateTime.now().plusDays(2));
        flight.setAvailableSeats(10);   // ✅ add this line (enough seats for diff=1)

        Booking booking = new Booking();
        booking.setPnr("PNR50000");
        booking.setName("Old Name");
        booking.setEmail("old@gmail.com");
        booking.setSeatsBooked(1);
        booking.setMealType("VEG");
        booking.setFlight(flight);

        Passenger oldPassenger = new Passenger();
        oldPassenger.setName("Old Passenger");
        booking.setPassengers(List.of(oldPassenger));

        when(bookingRepo.findByPnr("PNR50000")).thenReturn(booking);
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        BookingRequest req = new BookingRequest();
        req.name = "New Name";
        req.email = "new@gmail.com";
        req.seats = 2;
        req.mealType = "NON_VEG";

        PassengerRequest p1 = new PassengerRequest();
        p1.name = "P1";
        p1.gender = "Male";
        p1.age = 30;
        p1.seatNumber = "10A";

        PassengerRequest p2 = new PassengerRequest();
        p2.name = "P2";
        p2.gender = "Female";
        p2.age = 28;
        p2.seatNumber = "10B";

        req.passengers = List.of(p1, p2);

        Booking result = service.updateBooking("PNR50000", req);

        assertEquals("New Name", result.getName());
        assertEquals("new@gmail.com", result.getEmail());
        assertEquals("NON_VEG", result.getMealType());
    }


    @Test
    void testUpdateBookingTooLate() {

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDepartureTime(LocalDateTime.now().plusHours(5)); // < 24 hrs

        Booking booking = new Booking();
        booking.setPnr("PNR555");
        booking.setFlight(flight);

        when(bookingRepo.findByPnr("PNR555")).thenReturn(booking);

        BookingRequest req = new BookingRequest();
        req.name = "Test";
        req.email = "test@gmail.com";
        req.seats = 1;
        req.mealType = "VEG";

        assertThrows(RuntimeException.class, () -> {
            service.updateBooking("PNR555", req);
        });
    }
    
    @Test
    void testUpdateBookingAdjustsAvailableSeatsWhenSeatsIncrease() {

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDepartureTime(LocalDateTime.now().plusDays(2));
        flight.setAvailableSeats(5);  // currently 5 free seats

        Booking booking = new Booking();
        booking.setPnr("PNR70000");
        booking.setSeatsBooked(1);    // previously booked 1
        booking.setFlight(flight);

        when(bookingRepo.findByPnr("PNR70000")).thenReturn(booking);
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        BookingRequest req = new BookingRequest();
        req.name = "Test Name";
        req.email = "test@gmail.com";
        req.seats = 3;                // user now wants 3 seats (diff = +2)
        req.mealType = "VEG";

        PassengerRequest p1 = new PassengerRequest();
        p1.name = "P1";
        p1.gender = "Male";
        p1.age = 30;
        p1.seatNumber = "10A";

        PassengerRequest p2 = new PassengerRequest();
        p2.name = "P2";
        p2.gender = "Female";
        p2.age = 28;
        p2.seatNumber = "10B";

        PassengerRequest p3 = new PassengerRequest();
        p3.name = "P3";
        p3.gender = "Male";
        p3.age = 26;
        p3.seatNumber = "10C";

        req.passengers = List.of(p1, p2, p3);

        Booking result = service.updateBooking("PNR70000", req);

        assertEquals(3, result.getSeatsBooked());
        // availableSeats: 5 - (3 - 1) = 3
        assertEquals(3, flight.getAvailableSeats());
        verify(flightRepo, times(1)).save(flight);
    }

    //updateBooking fails if not enough seats for increase
    @Test
    void testUpdateBookingNotEnoughSeatsForIncrease() {

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDepartureTime(LocalDateTime.now().plusDays(2));
        flight.setAvailableSeats(1);  // only 1 free seat

        Booking booking = new Booking();
        booking.setPnr("PNR80000");
        booking.setSeatsBooked(3);    // already booked 3
        booking.setFlight(flight);

        when(bookingRepo.findByPnr("PNR80000")).thenReturn(booking);

        BookingRequest req = new BookingRequest();
        req.name = "Test Name";
        req.email = "test@gmail.com";
        req.seats = 6;   // wants 6 total => diff = +3, but only 1 free
        req.mealType = "VEG";

        assertThrows(RuntimeException.class, () -> service.updateBooking("PNR80000", req));

        // availableSeats should be unchanged
        assertEquals(1, flight.getAvailableSeats());
        verify(flightRepo, never()).save(flight);
        verify(bookingRepo, never()).save(any(Booking.class));
    }
}