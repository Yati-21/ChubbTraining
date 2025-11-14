package com.chubb.controller;
import com.chubb.request.Order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController    // makes this class as spring bean
public class OrderController   //receive http req
{
	@GetMapping("/order")   // add a unique path ()
	String getOrder()
	{
		return "hello";
	}
	
	
	@PostMapping("/order")
	Order saveOrder(@RequestBody @Valid Order order)
	{
		order.setTotalAmount(order.getPrice()*order.getQuantity());
		return order;
	}
	
}
