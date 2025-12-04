package com.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer 
{
	@KafkaListener(topics = "my-topic",groupId = "my-group")   //check if my-group exists -if not, craete automatically
	public void listen(String message)
	{
		System.out.println("Received msg: "+message);
	}

}
