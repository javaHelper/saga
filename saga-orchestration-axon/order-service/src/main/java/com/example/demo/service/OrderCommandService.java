package com.example.demo.service;

import java.util.concurrent.CompletableFuture;

import com.example.demo.dto.command.OrderCreateDTO;

public interface OrderCommandService {
	public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO);
}