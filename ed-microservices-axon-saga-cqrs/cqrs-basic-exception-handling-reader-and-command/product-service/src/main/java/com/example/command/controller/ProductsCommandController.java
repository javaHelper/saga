package com.example.command.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.command.CreateProductCommand;
import com.example.model.CreateProductRestModel;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsCommandController {
	@Autowired
	private Environment environment;
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

//	@PutMapping
//	public String updateProduct() {
//		return "HTTP PUT handled";
//	}
//
//	@GetMapping
//	public String getProduct() {
//		return "HTTP GET handled";
//	}
//
//	@DeleteMapping
//	public String deleteProduct() {
//		return "HTTP DELETE handled";
//	}
}
