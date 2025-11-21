package com.chubb.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.MockMvc;

import com.chubb.entity.AirportCode;
import com.chubb.entity.Booking;
import com.chubb.entity.Flight;
import com.chubb.entity.MealType;
import com.chubb.exception.BusinessException;
import com.chubb.exception.NotFoundException;
import com.chubb.exception.SeatUnavailableException;
import com.chubb.request.BookingRequest;
import com.chubb.request.FlightSearchRequest;
import com.chubb.request.PassengerRequest;
import com.chubb.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FlightController.class)
class FlightControllerTest 
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSearchFlightsSuccess() throws Exception 
    {
        Flight flight=new Flight();
        flight.setId(1L);
        flight.setAirline("TestAir");
        flight.setFlightNumber("TA101");
        flight.setFromCity(AirportCode.DEL);
        flight.setToCity(AirportCode.BOM);
        flight.setAvailableSeats(100);

        LocalDate date = LocalDate.of(2025,5,10);

        when(service.searchFlights("DEL","BOM", date)).thenReturn(List.of(flight));

        FlightSearchRequest req =new FlightSearchRequest();
        req.setFrom("DEL");
        req.setTo("BOM");
        req.setJourneyDate(date);

        mockMvc.perform(post("/api/v1.0/flight/search").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
        	.andExpect(status().isOk()).andExpect(jsonPath("$[0].flightNumber").value("TA101"));
    }

    @Test
    void testSearchFlights_ValidationError() throws Exception 
    {
        FlightSearchRequest req = new FlightSearchRequest();
        req.setFrom(""); //invalid
        req.setTo("BOM");
        req.setJourneyDate(null); //invalid

        mockMvc.perform(post("/api/v1.0/flight/search")
        		.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.from").exists()).andExpect(jsonPath("$.journeyDate").exists());
    }

    @Test
    void testSearchFlights_InvalidAirport() throws Exception 
    {
        FlightSearchRequest req=new FlightSearchRequest();
        req.setFrom("XXX");
        req.setTo("BOM");
        req.setJourneyDate(LocalDate.now());

        when(service.searchFlights("XXX","BOM", LocalDate.now()))
        	.thenThrow(new BusinessException("Invalid airport code: XXX"));

        mockMvc.perform(post("/api/v1.0/flight/search")
        		.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Invalid airport code: XXX"));
    }

    @Test
    void testBookTicketSuccess() throws Exception 
    {
        when(service.bookTicket(eq(1L),any())).thenReturn("PNR12345");

        BookingRequest req =validBookingRequest();

        mockMvc.perform(post("/api/v1.0/flight/booking/1")
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andExpect(content().string("PNR12345"));
    }

    @Test
    void testBookTicket_ValidationFailure() throws Exception 
    {
        BookingRequest req =new BookingRequest();
        req.setName(""); //invalid
        req.setEmail("invalid email");

        mockMvc.perform(post("/api/v1.0/flight/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void testGetTicketSuccess() throws Exception 
    {
        Booking b = new Booking();
        b.setPnr("PNR50000");
        b.setName("User");
        when(service.getTicket("PNR50000")).thenReturn(b);

        mockMvc.perform(get("/api/v1.0/flight/ticket/PNR50000"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.pnr").value("PNR50000"));
    }

    @Test
    void testGetTicket_NotFound() throws Exception 
    {
        when(service.getTicket("PNR999"))
            .thenThrow(new NotFoundException("PNR not found"));

        mockMvc.perform(get("/api/v1.0/flight/ticket/PNR999"))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value("PNR not found"));
    }


    @Test
    void testUpdateBookingSuccess() throws Exception 
    {
        Booking b =new Booking();
        b.setPnr("PNR123");
        b.setName("Updated User");

        when(service.updateBooking(eq("PNR123"),any())).thenReturn(b);

        mockMvc.perform(put("/api/v1.0/flight/booking/update/PNR123")
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(validBookingRequest())))
            .andExpect(status().isOk()).andExpect(jsonPath("$.pnr").value("PNR123"));
    }

    @Test
    void testUpdateBooking_SeatUnavailable() throws Exception 
    {
        when(service.updateBooking(eq("PNR500"),any()))
            .thenThrow(new SeatUnavailableException("Seat already booked"));
        mockMvc.perform(put("/api/v1.0/flight/booking/update/PNR500")
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(validBookingRequest())))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Seat already booked"));
    }


    @Test
    void testCancelBookingSuccess() throws Exception 
    {
        when(service.cancelBooking("PNR111")).thenReturn("Booking cancelled successfully: PNR111");
        mockMvc.perform(delete("/api/v1.0/flight/booking/cancel/PNR111"))
            .andExpect(status().isOk())
            .andExpect(content().string("Booking cancelled successfully: PNR111"));
    }

    @Test
    void testCancelBooking_NotFound() throws Exception 
    {
        when(service.cancelBooking("PNR999"))
            .thenThrow(new NotFoundException("Invalid PNR"));

        mockMvc.perform(delete("/api/v1.0/flight/booking/cancel/PNR999")).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value("Invalid PNR"));
    }

    @Test
    void testGetBookingHistory() throws Exception 
    {
        Booking b=new Booking();
        b.setPnr("PNR123");

        when(service.getBookingHistory("abc@test.com")).thenReturn(List.of(b));

        mockMvc.perform(get("/api/v1.0/flight/booking/history/abc@test.com"))
            .andExpect(status().isOk()).andExpect(jsonPath("$[0].pnr").value("PNR123"));
    }

    private BookingRequest validBookingRequest() 
    {
        BookingRequest req =new BookingRequest();
        req.setName("User");
        req.setEmail("user@test.com");
        req.setSeats(1);
        req.setMealType(MealType.VEG);

        PassengerRequest p =new PassengerRequest();
        p.setName("P1");
        p.setGender("M");
        p.setAge(20);
        p.setSeatNumber("A1");

        req.setPassengers(List.of(p));
        return req;
    }
}