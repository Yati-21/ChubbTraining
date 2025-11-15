package com.chubb.service;

import org.springframework.stereotype.Service;

import com.chubb.request.Order;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService //to implement business logic
{
	public void insertOrder(Order order) 
	{
		order.setTotalAmount(order.getPrice()*order.getQuantity());
		System.out.println(order);
	
//		log.debug(order.toString());
	}

}
