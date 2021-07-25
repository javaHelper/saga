package com.example.query;

import com.example.core.commands.events.ProductReservedEvents;
import com.example.core.data.ProductEntity;
import com.example.core.event.ProductCreatedEvent;
import com.example.core.events.ProductReservationCancelledEvent;
import com.example.repository.ProductsRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventHandler.class);

    @Autowired
    private ProductsRepository productsRepository;

    
    // This is best part to learn in Saga
    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception ex) throws Exception {
        throw ex;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException ex) {
        throw ex;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, productEntity);

        try {
            productsRepository.save(productEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void on(ProductReservedEvents productReservedEvents) {
        ProductEntity productEntity = productsRepository.findByProductId(productReservedEvents.getProductId());
        
        LOGGER.debug("### ProductReservedEvents: Current Product Quantity ="+productEntity.getQuantity());
        
        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvents.getQuantity());
        productsRepository.save(productEntity);

        LOGGER.debug("### ProductReservedEvents: New Product Quantity ="+productEntity.getQuantity());
        
        LOGGER.info("### ProductReservedEvent is called for productId: "+ productReservedEvents.getOrderId()+
                "and ProductId: "+productReservedEvents.getProductId());
    }
    
    
    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
    	ProductEntity currentlyStoredProduct = productsRepository.findByProductId(productReservationCancelledEvent.getProductId());
    	
    	LOGGER.debug("### ProductReservationCancelledEvent: Current Product Quantity ="+currentlyStoredProduct.getQuantity());
    	 
    	int newQuantity = currentlyStoredProduct.getQuantity() + productReservationCancelledEvent.getQuantity();
    	currentlyStoredProduct.setQuantity(newQuantity);
    	
    	productsRepository.save(currentlyStoredProduct);
    	
    	LOGGER.debug("### ProductReservationCancelledEvent: New Product Quantity ="+currentlyStoredProduct.getQuantity());
    }
}
