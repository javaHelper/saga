package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.dto.OrchestratorRequestDTO;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

@Configuration
public class OrderConfig {

	// Dispatches onNext, onError and onComplete signals to zero-to-many Subscribers.Please note, that along with multiple consumers, 
	// current implementation ofDirectProcessor supports multiple producers. However, all producers must produce messages on the 
	// same Thread, otherwise Reactive Streams Spec contract is violated. 
    @Bean
    public DirectProcessor<OrchestratorRequestDTO> publisher(){
        return DirectProcessor.create();
    }

    @Bean
    public FluxSink<OrchestratorRequestDTO> sink(DirectProcessor<OrchestratorRequestDTO> publisher){
        return publisher.sink();
    }

}