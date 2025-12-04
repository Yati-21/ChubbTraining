package com.example.demo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
    private String id;
    
    private String name;
    private double price;
}
