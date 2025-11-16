package com.chubb.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.test.web.servlet.MockMvc;

import com.chubb.entity.AirportCode;
import com.chubb.entity.Booking;
import com.chubb.entity.Flight;
import com.chubb.request.BookingRequest;
import com.chubb.request.FlightSearchRequest;
import com.chubb.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSearchFlights() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAirline("IndiGo");
        flight.setFlightNumber("6E101");
        flight.setFromCity(AirportCode.DEL);
        flight.setToCity(AirportCode.BOM);
        flight.setTotalSeats(180);
        flight.setAvailableSeats(180);
        flight.setPrice(4500);

        LocalDate journeyDate = LocalDate.of(2025, 12, 20);

        when(service.searchFlights("DEL", "BOM", journeyDate))
                .thenReturn(List.of(flight));

        FlightSearchRequest req = new FlightSearchRequest();
        req.from = "DEL";
        req.to = "BOM";
        req.journeyDate = journeyDate;

        mockMvc.perform(
                    post("/api/v1.0/flight/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("6E101"))
                .andExpect(jsonPath("$[0].airline").value("IndiGo"))
                .andExpect(jsonPath("$[0].fromCity").value("DEL"))
                .andExpect(jsonPath("$[0].toCity").value("BOM"));
    }

    @Test
    void testGetTicket() throws Exception {
        Booking booking = new Booking();
        booking.setPnr("PNR12345");
        booking.setName("Ravi");
        booking.setEmail("ravi@gmail.com");
        booking.setSeatsBooked(2);

        when(service.getTicket("PNR12345")).thenReturn(booking);

        mockMvc.perform(get("/api/v1.0/flight/ticket/PNR12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pnr").value("PNR12345"))
                .andExpect(jsonPath("$.name").value("Ravi"))
                .andExpect(jsonPath("$.email").value("ravi@gmail.com"));
    }

    @Test
    void testUpdateBooking() throws Exception {
        Booking updated = new Booking();
        updated.setPnr("PNR50000");
        updated.setName("Updated Name");
        updated.setEmail("updated@gmail.com");
        updated.setSeatsBooked(1);
        updated.setMealType("VEG");

        when(service.updateBooking(eq("PNR50000"), any(BookingRequest.class)))
                .thenReturn(updated);

        BookingRequest req = new BookingRequest();
        req.name = "Updated Name";
        req.email = "updated@gmail.com";
        req.seats = 1;
        req.mealType = "VEG";

        mockMvc.perform(
                    put("/api/v1.0/flight/booking/update/PNR50000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pnr").value("PNR50000"))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@gmail.com"))
                .andExpect(jsonPath("$.mealType").value("VEG"));
    }
}