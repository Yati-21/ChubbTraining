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
import com.chubb.entity.MealType;
import com.chubb.entity.Passenger;
import com.chubb.exception.BusinessException;
import com.chubb.exception.NotFoundException;
import com.chubb.exception.SeatUnavailableException;
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
	void testSearchFlights() 
	{
		Flight flight=new Flight();
		flight.setId(1L);
		flight.setFromCity(AirportCode.DEL);
		flight.setToCity(AirportCode.BOM);

		//mocking flightRepo to return a list with one flight
		when(flightRepo.findByFromCityAndToCity(AirportCode.DEL, AirportCode.BOM)).thenReturn(List.of(flight));

		LocalDate journeyDate=LocalDate.of(2025,12,20);
		flight.setDepartureTime(journeyDate.atTime(9,0)); 
		List<Flight> result=service.searchFlights("DEL","BOM", journeyDate);

		assertEquals(1,result.size());
		assertEquals(1L,result.get(0).getId());
	}

	@Test
	void testBookTicket() 
	{
		Flight flight=new Flight();
		flight.setId(1L);
		flight.setAvailableSeats(10);

		//mocking flightRepo to return the flight when searched by ID
		when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

		BookingRequest req =new BookingRequest();
		req.setName("User1");
		req.setEmail("user1@test.com");
		req.setSeats(1);
		req.setMealType(MealType.VEG);

		PassengerRequest p1 =new PassengerRequest();
		p1.setName("Passenger1");
		p1.setGender("M");
		p1.setAge(28);
		p1.setSeatNumber("A1");

		req.setPassengers(List.of(p1));

		Booking booking =new Booking();
		booking.setPnr("PNR99999");
		booking.setMealType(MealType.VEG);

		//mocking bookingRepo to return the booking when saved
		when(bookingRepo.save(any(Booking.class))).thenReturn(booking);
		String pnr =service.bookTicket(1L, req);
		assertEquals("PNR99999", pnr);
	}

	@Test
	void testBookTicketPassengerCountMismatchThrows() 
	{
		Flight flight=new Flight();
		flight.setId(1L);
		flight.setAvailableSeats(10);

		//mocking flightRepo to return the flight when searched by ID
		when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

		BookingRequest req=new BookingRequest();
		req.setName("User1");
		req.setEmail("user1@test.com");
		// set seats to 2, but provide only 1 passenger -> should throw
		// BusinessException
		req.setSeats(2);
		req.setMealType(MealType.VEG);

		PassengerRequest p1 =new PassengerRequest();
		p1.setName("Passenger1");
		p1.setGender("M");
		p1.setAge(25);
		p1.setSeatNumber("A1");
		req.setPassengers(List.of(p1));

		// Expect BusinessException because passenger count and seats mismatch
		assertThrows(BusinessException.class,()-> service.bookTicket(1L,req));

		// seats should NOT be changed in this case
		assertEquals(10,flight.getAvailableSeats());
		verify(bookingRepo,never()).save(any(Booking.class));
	}

	@Test
	void testCancelBookingSuccess() {

		Flight flight =new Flight();
		flight.setId(1L);
		flight.setAvailableSeats(50);
		flight.setDepartureTime(LocalDateTime.now().plusDays(2)); //>24 hrs
		
		Booking booking =new Booking();
		booking.setPnr("PNR111");
		booking.setSeatsBooked(2);
		booking.setFlight(flight);

		//mocking bookingRepo to return the booking
		when(bookingRepo.findByPnr("PNR111")).thenReturn(booking);

		String msg =service.cancelBooking("PNR111");

		assertTrue(msg.contains("successfully"));
		verify(bookingRepo, times(1)).delete(booking);
		verify(flightRepo, times(1)).save(flight);
	}

	@Test
	void testCancelBookingTooLate() {

		Flight flight =new Flight();
		flight.setId(1L);
		flight.setAvailableSeats(50);
		flight.setDepartureTime(LocalDateTime.now().plusHours(10)); //<24 hrs
		Booking booking =new Booking();
		booking.setPnr("PNR222");
		booking.setSeatsBooked(1);
		booking.setFlight(flight);

		//mocking bookingRepo to return the booking
		when(bookingRepo.findByPnr("PNR222")).thenReturn(booking);

		//cancel within 24 hours should throw BusinessException
		assertThrows(BusinessException.class, ()-> 
		{
			service.cancelBooking("PNR222");
		});
	}

	@Test
	void testUpdateBooking() 
	{

		Flight flight =new Flight();
		flight.setId(1L);
		flight.setDepartureTime(LocalDateTime.now().plusDays(2));
		flight.setAvailableSeats(10); 
		Booking booking =new Booking();
		booking.setPnr("PNR50000");
		booking.setName("OldUser");
		booking.setEmail("olduser@test.com");
		booking.setSeatsBooked(1);
		booking.setMealType(MealType.VEG);
		booking.setFlight(flight);

		Passenger oldPassenger=new Passenger();
		oldPassenger.setName("Old Passenger");
		booking.setPassengers(List.of(oldPassenger));

		//mocking bookingRepo to return the booking
		when(bookingRepo.findByPnr("PNR50000")).thenReturn(booking);
		when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

		BookingRequest req=new BookingRequest();
		req.setName("UpdatedUser");
		req.setEmail("updated@test.com");
		req.setSeats(2);
		req.setMealType(MealType.NON_VEG);

		PassengerRequest p1=new PassengerRequest();
		p1.setName("PassengerA");
		p1.setGender("M");
		p1.setAge(30);
		p1.setSeatNumber("A1");

		PassengerRequest p2=new PassengerRequest();
		p2.setName("PassengerB");
		p2.setGender("F");
		p2.setAge(28);
		p2.setSeatNumber("A2");

		req.setPassengers(List.of(p1, p2));

		Booking result=service.updateBooking("PNR50000", req);
		assertEquals("UpdatedUser", result.getName());
		assertEquals("updated@test.com", result.getEmail());
		assertEquals(MealType.NON_VEG, result.getMealType());

	}

	@Test
	void testUpdateBookingTooLate() 
	{

		Flight flight =new Flight();
		flight.setId(1L);
		flight.setDepartureTime(LocalDateTime.now().plusHours(5)); // < 24 hrs

		Booking booking =new Booking();
		booking.setPnr("PNR555");
		booking.setFlight(flight);

		//mocking bookingRepo to return the booking
		when(bookingRepo.findByPnr("PNR555")).thenReturn(booking);

		BookingRequest req =new BookingRequest();
		req.setName("Test");
		req.setEmail("test@gmail.com");
		req.setSeats(1);
		req.setMealType(MealType.VEG);

		//updating within 24 hours should throw BusinessException
		assertThrows(BusinessException.class, () -> {
			service.updateBooking("PNR555", req);
		});
	}

	@Test
	void testUpdateBookingAdjustsAvailableSeatsWhenSeatsIncrease() {

		Flight flight =new Flight();
		flight.setId(1L);
		flight.setDepartureTime(LocalDateTime.now().plusDays(2));
		flight.setAvailableSeats(5); //currently 5 free seats

		Booking booking =new Booking();
		booking.setPnr("PNR70000");
		booking.setSeatsBooked(1); //previously booked 1
		booking.setFlight(flight);

		//mocking bookingRepo to return the booking
		when(bookingRepo.findByPnr("PNR70000")).thenReturn(booking);
		//mocking save
		when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

		BookingRequest req =new BookingRequest();
		req.setName("UserX");
		req.setEmail("userx@test.com");
		req.setSeats(3); //user now wants 3 seats (diff =+2)
		req.setMealType(MealType.VEG);

		PassengerRequest p1 =new PassengerRequest();
		p1.setName("PassengerA");
		p1.setGender("M");
		p1.setAge(30);
		p1.setSeatNumber("A1");

		PassengerRequest p2 =new PassengerRequest();
		p2.setName("PassengerB");
		p2.setGender("F");
		p2.setAge(28);
		p2.setSeatNumber("A2");

		PassengerRequest p3 =new PassengerRequest();
		p3.setName("PassengerC");
		p3.setGender("M");
		p3.setAge(26);
		p3.setSeatNumber("A3");

		req.setPassengers(List.of(p1, p2, p3));

		Booking result =service.updateBooking("PNR70000", req);

		assertEquals(3, result.getSeatsBooked());
		// availableSeats: 5 -(3-1) =3
		assertEquals(3, flight.getAvailableSeats());
		verify(flightRepo, times(1)).save(flight);
	}

	//updateBooking fails if not enough seats for increase
	@Test
	void testUpdateBookingNotEnoughSeatsForIncrease() {

		Flight flight =new Flight();
		flight.setId(1L);
		flight.setDepartureTime(LocalDateTime.now().plusDays(2));
		flight.setAvailableSeats(1); //only 1 free seat

		Booking booking =new Booking();
		booking.setPnr("PNR80000");
		booking.setSeatsBooked(3); //already booked 3
		booking.setFlight(flight);

		//mocking bookingRepo to return the booking
		when(bookingRepo.findByPnr("PNR80000")).thenReturn(booking);

		BookingRequest req =new BookingRequest();
		req.setName("Test Name");
		req.setEmail("test@gmail.com");
		req.setSeats(6); // wants 6 total-> diff=3,but only 1 free
		req.setMealType(MealType.VEG);

		//should throw SeatUnavailableException per service logic
		assertThrows(SeatUnavailableException.class, () -> service.updateBooking("PNR80000", req));

		//availableSeats should be unchanged
		assertEquals(1, flight.getAvailableSeats());
		verify(flightRepo, never()).save(flight);
		verify(bookingRepo, never()).save(any(Booking.class));
	}
	@Test
	void testGetTicket_NotFoundThrows() {
	    when(bookingRepo.findByPnr("INVALID")).thenReturn(null);
	    assertThrows(NotFoundException.class, () -> service.getTicket("INVALID"));
	}
	
	
	@Test
	void testBookTicket_NotEnoughSeats() {
	    Flight flight =new Flight();
	    flight.setId(1L);
	    flight.setAvailableSeats(1); 

	    //mocking flightRepo to return the flight when searched by ID
	    when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

	    BookingRequest req =new BookingRequest();
	    req.setName("User");
	    req.setEmail("user@test.com");
	    req.setSeats(2); 
	    req.setMealType(MealType.VEG);


	    PassengerRequest p1 =new PassengerRequest();
	    p1.setName("A");
	    p1.setGender("M");
	    p1.setAge(20);
	    p1.setSeatNumber("A1");

	    PassengerRequest p2 =new PassengerRequest();
	    p2.setName("B");
	    p2.setGender("F");
	    p2.setAge(21);
	    p2.setSeatNumber("A2");

	    req.setPassengers(List.of(p1, p2));

	    assertThrows(SeatUnavailableException.class,() -> service.bookTicket(1L, req));
	}
	@Test
	void testDuplicatePassengerThrowsException() {
	    Flight flight =new Flight();
	    flight.setId(1L);
	    flight.setDepartureTime(LocalDateTime.now().plusDays(1));
	    flight.setAvailableSeats(5);

	    when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

	    BookingRequest req =new BookingRequest();
	    req.setName("Test");
	    req.setEmail("test@gmail.com");
	    req.setSeats(2);
	    req.setMealType(MealType.VEG);

	    PassengerRequest p1 =new PassengerRequest();
	    p1.setName("john");
	    p1.setGender("M");
	    p1.setAge(25);
	    p1.setSeatNumber("A1");

	    PassengerRequest p2=new PassengerRequest();
	    p2.setName("john");
	    p2.setGender("M");
	    p2.setAge(25);
	    p2.setSeatNumber("A2");

	    req.setPassengers(List.of(p1, p2));

	    assertThrows(BusinessException.class,()->service.bookTicket(1L, req),"Duplicate passenger detected");
	}

}