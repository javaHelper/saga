package com.example.command.rest;

import java.util.UUID;
import javax.validation.Valid;

import com.example.command.commands.CreateOrderCommand;
import com.example.core.data.OrderStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Sends the given command and wait for it to execute. The result of the execution is returned when available. 
 * This method will block indefinitely, until a result is available, or until the Thread is interrupted. 
 * When the thread is interrupted, this method returns null. If command execution resulted in an exception, 
 * it is wrapped in a CommandExecutionException. If command dispatching failed, CommandDispatchException is thrown instead. 
 *
 */

@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

    private final CommandGateway commandGateway;

    @Autowired
    public OrdersCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@Valid @RequestBody OrderCreateRest order) {
        
        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .addressId(order.getAddressId())
                .productId(order.getProductId())
                .userId(userId)
                .quantity(order.getQuantity())
                .orderId(UUID.randomUUID().toString())
                .orderStatus(OrderStatus.CREATED)
                .build();

        return commandGateway.sendAndWait(createOrderCommand);
    }
}