package com.chubb.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class FlightValidationTest 
{

    @Test
    void testIsDifferentCities_NullFrom() {
        Flight flight=new Flight();
        flight.setFromCity(null);
        flight.setToCity(AirportCode.BOM);
        assertFalse(flight.isDifferentCities());
    }

    @Test
    void testIsDifferentCities_NullTo() {
        Flight flight=new Flight();
        flight.setFromCity(AirportCode.DEL);
        flight.setToCity(null);
        assertFalse(flight.isDifferentCities());
    }

    @Test
    void testIsDifferentCities_SameCity() {
        Flight flight=new Flight();
        flight.setFromCity(AirportCode.DEL);
        flight.setToCity(AirportCode.DEL);
        assertFalse(flight.isDifferentCities());
    }

    @Test
    void testIsDifferentCities_DifferentCities() {
        Flight flight=new Flight();
        flight.setFromCity(AirportCode.DEL);
        flight.setToCity(AirportCode.BOM);
        assertTrue(flight.isDifferentCities());
    }

    @Test
    void testIsValidTimes_DepartureNull() {
        Flight flight=new Flight();
        flight.setDepartureTime(null);
        flight.setArrivalTime(LocalDateTime.now());
        assertTrue(flight.isValidTimes()); 
    }

    @Test
    void testIsValidTimes_ArrivalNull() {
        Flight flight=new Flight();
        flight.setDepartureTime(LocalDateTime.now());
        flight.setArrivalTime(null);
        assertTrue(flight.isValidTimes());
    }

    @Test
    void testIsValidTimes_ValidTimes() {
        Flight flight =new Flight();
        LocalDateTime dep =LocalDateTime.of(2025,1,1,10,0);
        LocalDateTime arr =LocalDateTime.of(2025,1,1,12,0);

        flight.setDepartureTime(dep);
        flight.setArrivalTime(arr);

        assertTrue(flight.isValidTimes());
    }

    @Test
    void testIsValidTimes_InvalidTimes() {
        Flight flight= new Flight();
        LocalDateTime dep =LocalDateTime.of(2025,1,1,10,0);
        LocalDateTime arr= LocalDateTime.of(2025,1,1,9,0); 
        flight.setDepartureTime(dep);
        flight.setArrivalTime(arr);

        assertFalse(flight.isValidTimes());
    }
}
