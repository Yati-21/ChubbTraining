package com.chubb.controller;
import com.chubb.request.Order1;
import com.chubb.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController    // makes this class as spring bean
public class OrderController   //receive http req
{
	@Autowired
	OrderService service;
	
	@GetMapping("/order/{id}")   //add a unique path ()
	Order1 getOrder(@PathVariable int id)
	{
//		return "hello";
		return service.getOrderById(id);
	}
	
	
//	@PostMapping("/order")
//	Order saveOrder(@RequestBody @Valid Order order)
//	{
//		order.setTotalAmount(order.getPrice()*order.getQuantity());
//		return order;
//	}
	@PostMapping("/order")
	Order1 saveOrder(@RequestBody @Valid Order1 order)
	{
		log.debug("Debug log:::logger added");
		return service.insertOrder(order);
		
	}
	
}
