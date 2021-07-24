package com.example.command;

import com.example.core.data.ProductLookupEntity;
import com.example.core.data.ProductLookupRepository;
import com.example.core.event.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventHandler {
    @Autowired
    private ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductLookupEntity entity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        productLookupRepository.save(entity);
    }
}
