package com.chubb.controller;
import com.chubb.request.Order;
import com.chubb.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController    // makes this class as spring bean
public class OrderController   //receive http req
{
	OrderService service;
	
	@GetMapping("/order")   // add a unique path ()
	String getOrder()
	{
		return "hello";
	}
	
	
//	@PostMapping("/order")
//	Order saveOrder(@RequestBody @Valid Order order)
//	{
//		order.setTotalAmount(order.getPrice()*order.getQuantity());
//		return order;
//	}
	@PostMapping("/order")
	Order saveOrder(@RequestBody @Valid Order order)
	{
		service.insertOrder(order);
		return order;
	}
	
}
