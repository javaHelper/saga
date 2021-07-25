package com.example.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.command.CreateProductCommand;
import com.example.model.CreateProductRestModel;

@RestController
@RequestMapping("/products")
public class ProductsCommandController {
	@Autowired
	private CommandGateway commandGateway;

	@PostMapping
	public String createProduct(@Valid @RequestBody CreateProductRestModel model) {
		
		CreateProductCommand command = CreateProductCommand.builder()
				.price(model.getPrice())
				.quantity(model.getQuantity())
				.title(model.getTitle())
				.productId(UUID.randomUUID().toString())
				.build();

		String returnValue = commandGateway.sendAndWait(command);;
		return returnValue;
	}
}
