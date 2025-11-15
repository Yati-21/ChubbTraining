package com.chubb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.repository.OrderRepository;
import com.chubb.request.Order1;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService //to implement business logic
{
	@Autowired
	OrderRepository orderRepository;
	public Order1 insertOrder(Order1 order) 
	{
//		order.setTotalAmount(order.getPrice()*order.getQuantity());
//		System.out.println(order);
		
		return orderRepository.save(order);
//		log.debug(order.toString());
	}
	
	public Order1 getOrderById(int id)
	{
		return orderRepository.findById(id).orElse(null);
		
	}

}
