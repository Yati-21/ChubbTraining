package com.chubb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubb.entity.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long> 
{

	boolean existsBySeatNumberAndBooking_Flight_Id(String seatNumber,Long flightId);

}
