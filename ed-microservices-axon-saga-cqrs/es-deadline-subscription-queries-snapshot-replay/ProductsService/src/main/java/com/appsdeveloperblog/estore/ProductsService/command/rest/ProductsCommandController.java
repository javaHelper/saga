package com.appsdeveloperblog.estore.ProductsService.command.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.ProductsService.command.CreateProductCommand;

@RestController
@RequestMapping("/products") // http://localhost:8080/products
public class ProductsCommandController {

		private final CommandGateway commandGateway;

	@Autowired
	public ProductsCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {

		CreateProductCommand createProductCommand = CreateProductCommand.builder()
				.price(createProductRestModel.getPrice()).quantity(createProductRestModel.getQuantity())
				.title(createProductRestModel.getTitle()).productId(UUID.randomUUID().toString()).build();

		String returnValue = commandGateway.sendAndWait(createProductCommand);
		return returnValue;
	}
}
