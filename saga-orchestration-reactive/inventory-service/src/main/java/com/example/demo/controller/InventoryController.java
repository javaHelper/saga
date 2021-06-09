package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.InventoryService;
import com.example.dto.InventoryRequestDTO;
import com.example.dto.InventoryResponseDTO;

@RestController
@RequestMapping("inventory")
public class InventoryController {

	@Autowired
	private InventoryService service;

	@PostMapping("/deduct")
	public InventoryResponseDTO deduct(@RequestBody final InventoryRequestDTO requestDTO) {
		return this.service.deductInventory(requestDTO);
	}

	@PostMapping("/add")
	public void add(@RequestBody final InventoryRequestDTO requestDTO) {
		this.service.addInventory(requestDTO);
	}

}