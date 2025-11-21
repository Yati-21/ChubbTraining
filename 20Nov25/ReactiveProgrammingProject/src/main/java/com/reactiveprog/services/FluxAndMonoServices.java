package com.reactiveprog.services;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoServices {
	
	public Flux<String> fruitsFlux()
	{
		return Flux.fromIterable(List.of("Mango","apple","banana")).log();
	}
	
	public Mono<String> fruitMono()
	{
		return Mono.just("zgrapes").log();
	}
	
	public static void main(String[] args)
	{
		FluxAndMonoServices fluxAndMonoServices = new FluxAndMonoServices();
		
		fluxAndMonoServices.fruitsFlux().subscribe(s-> {
			System.out.println("s: "+s);
		});	
		
		System.out.println();
		
		fluxAndMonoServices.fruitMono().subscribe(s-> {
			System.out.println("s: "+s);
		});
	}
}
