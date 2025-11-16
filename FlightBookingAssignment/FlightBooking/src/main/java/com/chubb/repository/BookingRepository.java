package com.chubb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubb.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByPnr(String pnr);
    List<Booking> findByEmail(String email);
}