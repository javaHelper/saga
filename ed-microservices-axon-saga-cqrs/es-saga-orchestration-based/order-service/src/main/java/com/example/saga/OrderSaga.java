package com.example.saga;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.command.commands.ApproveOrderCommand;
import com.example.command.commands.RejectOrderCommand;
import com.example.core.commands.CancelProductReservationCommand;
import com.example.core.commands.ProcessPaymentCommand;
import com.example.core.commands.ReserveProductCommand;
import com.example.core.commands.events.ProductReservedEvents;
import com.example.core.events.OrderApprovedEvent;
import com.example.core.events.OrderCreatedEvent;
import com.example.core.events.OrderRejectedEvent;
import com.example.core.events.PaymentProcessedEvent;
import com.example.core.events.ProductReservationCancelledEvent;
import com.example.core.model.User;
import com.example.core.query.FetchUserPaymentDetailsQuery;

@Saga
public class OrderSaga {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();

        LOGGER.info("OrderCreatedEvent handled for OrderId: " + reserveProductCommand.getOrderId() +
                "and ProductId: " + reserveProductCommand.getProductId());

        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {
            @Override
            public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
                                 CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    // Start compensating transaction
                }
            }
        });
    }


    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvents productReservedEvents) {
        // Process user Payment
        LOGGER.info("ProductReservedEvent is called for productId: " + productReservedEvents.getOrderId() +
                "and ProductId: " + productReservedEvents.getProductId());

        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvents.getUserId());
        User userPaymentDetails = null;
        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception ex) {
            LOGGER.error("" + ex.getMessage());

            // start compensating transaction
            cancelProductReservation(productReservedEvents, ex.getMessage());
            return;
        }

        if (userPaymentDetails == null) {
            // Start Compensating Tx
        	cancelProductReservation(productReservedEvents, "Could not fetched User payment details");
            return;
        }

        LOGGER.info("Successfully fetched user payment details for user " + userPaymentDetails.getFirstName());

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvents.getOrderId())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 20, TimeUnit.SECONDS);
        }catch (Exception ex){
            LOGGER.error(""+ex.getMessage());
            // Start compensating tx
            cancelProductReservation(productReservedEvents, ex.getMessage());
            return;
        }

        if(result == null){
            LOGGER.info("The ProcessPaymentCommand resulted is NULL. Initiating a compensating transaction...");
            // Start Compensating Transaction
            cancelProductReservation(productReservedEvents, "Could not process user payment with provided payment details..");
        }
    }
    
    
    public void cancelProductReservation(ProductReservedEvents productReservedEvents, String reason) {
    	CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
    			.orderId(productReservedEvents.getOrderId())
    			.productId(productReservedEvents.getProductId())
    			.quantity(productReservedEvents.getQuantity())
    			.userId(productReservedEvents.getUserId())
    			.reason(reason)
    			.build();
    	
    	commandGateway.send(cancelProductReservationCommand);
    }
    
    

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent){
        // Send an Approved Order Command
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent){
        LOGGER.info("Order is approved. Order Saga is completed for orderId: "+orderApprovedEvent.getOrderId());
       // SagaLifecycle.end();
    }
    
    
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
    	// Create and send a RejectOrderCommand
    	RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(),
    			productReservationCancelledEvent.getReason());
    	
    	commandGateway.send(rejectOrderCommand);
    }
    
    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent) {
    	LOGGER.info("Successfully rejected order with Id "+ orderRejectedEvent.getOrderId());
    }
    
}
