package com.example.demo.eventhandlers;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.OrderEventUpdateService;
import com.example.dto.OrchestratorRequestDTO;
import com.example.dto.OrchestratorResponseDTO;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

@Configuration
public class OrderEventHandler {

    @Autowired
    private DirectProcessor<OrchestratorRequestDTO> source;

    @Autowired
    private OrderEventUpdateService service;

    @Bean
    public Supplier<Flux<OrchestratorRequestDTO>> supplier(){
        return () -> Flux.from(source);
    };

    @Bean
    public Consumer<Flux<OrchestratorResponseDTO>> consumer(){
        return (flux) -> flux
                            .subscribe(responseDTO -> this.service.updateOrder(responseDTO));
    };

}