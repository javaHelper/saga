package com.example.query;

import com.example.core.events.OrderApprovedEvent;
import com.example.core.events.OrderCreatedEvent;
import com.example.core.events.OrderRejectedEvent;
import com.example.core.model.OrderEntity;
import com.example.core.model.OrdersRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
    
	@Autowired
    private OrdersRepository ordersRepository;
    
    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);
        this.ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent){
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());
        if (orderEntity == null){
            // TODO - Do something about it
            return;
        }
        orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
        ordersRepository.save(orderEntity);
    }
    
    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
    	OrderEntity orderEntity = ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());
    	orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
    	
    	ordersRepository.save(orderEntity);
    }
    
}