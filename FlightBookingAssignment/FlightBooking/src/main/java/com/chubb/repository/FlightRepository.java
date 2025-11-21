package com.chubb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubb.entity.AirportCode;
import com.chubb.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long> 
{

    List<Flight> findByFromCityAndToCity(AirportCode fromCity,AirportCode toCity);
}