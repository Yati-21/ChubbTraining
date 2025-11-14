package com.chubb.service;

import org.springframework.stereotype.Service;

import com.chubb.request.Order;

@Service
public class OrderService //to implement business logic
{
	public void insertOrder(Order order) 
	{
		System.out.println(order);
	}

}
