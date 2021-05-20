package com.vinsguru.order.config;

import com.vinsguru.events.order.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class OrderConfig {

	// Help building Sinks.Many sinks that will broadcast multiple signals to one or more Subscriber. 
	// Use Many.asFlux() to expose the Flux view of the sink to the downstream consumers.
    @Bean
    public Sinks.Many<OrderEvent> orderSink(){
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    // Return a Flux view of this sink. Every call returns the same instance
    // Sinks.Many<OrderEvent> - A base interface for standalone Sinks with Flux semantics. 
    @Bean
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sink){
        return sink::asFlux;
    }
}
