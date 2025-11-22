package com.mongodb.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;



//gfg code!!! - https://www.geeksforgeeks.org/advance-java/handling-errors-in-spring-webflux/

@Service
public class Servicehandler {
  
  // api for mono with on error return method
    public Mono<ServerResponse> errorHandlingAtFunctionalLevelWithMono(ServerRequest request){
        return sayHello(request)
                .onErrorReturn("This is Error Handling At Functional Level with Mono with onErrorReturn")
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(response));
    }
  // api for flux with on error resume method  
    public Mono<ServerResponse> errorHandlingAtFunctionalLevelWithFlux(ServerRequest request){
        return sayHello(request)
                .onErrorResume(error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                    return Mono.just("This is Error Handling At Functional Level With Flux and onErrorResume()");
                })
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(response));
    }

  // used for creating a runtime error
    public Mono<String> sayHello(ServerRequest request) {
         return Mono.error(new RuntimeException("Exception is Raised"));
    }
    
}