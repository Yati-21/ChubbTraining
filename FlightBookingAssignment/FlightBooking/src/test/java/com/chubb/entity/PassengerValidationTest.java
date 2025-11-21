package com.chubb.entity;

import com.chubb.request.PassengerRequest;
import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PassengerValidationTest 
{

    private static Validator validator;

    @BeforeAll
    public static void setup() 
    {
        ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    @Test
    void testInvalidGender() 
    {
        PassengerRequest p=new PassengerRequest();
        p.setName("A");
        p.setGender("X");
        p.setAge(20);
        p.setSeatNumber("A1");

        Set<ConstraintViolation<PassengerRequest>> v=validator.validate(p);
        assertTrue(v.stream().anyMatch(e ->e.getMessage().contains("Gender must be M/F/O")));
    }


    @Test
    void testValidPassenger() 
    {
        PassengerRequest p = new PassengerRequest();
        p.setName("John");
        p.setGender("M");
        p.setAge(30);
        p.setSeatNumber("A1");

        Set<ConstraintViolation<PassengerRequest>> v=validator.validate(p);
        assertTrue(v.isEmpty());
    }
}
