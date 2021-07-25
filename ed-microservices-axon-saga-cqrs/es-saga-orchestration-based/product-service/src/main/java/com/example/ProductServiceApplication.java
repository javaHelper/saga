package com.example;

import com.example.command.interceptors.CreateProductCommandInterceptor;
import com.example.core.data.errorhandling.ProductsServiceEventsHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    // Register the given DispatchInterceptor. After registration, the interceptor will be invoked for each Messagedispatched on the 
    // messaging component that it was registered to.
    @Autowired
    public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }

    // Register the given DispatchInterceptor. After registration, the interceptor will be invoked for each Messagedispatched 
    // on the messaging component that it was registered to.
    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler("product-group",
                conf -> new ProductsServiceEventsHandler());

//        configurer.registerListenerInvocationErrorHandler("product-group",
//                conf -> PropagatingErrorHandler.instance());
    }
}
