package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.dto.OrderDTO;

@Document
public class Order extends OrderDTO {

	@Override
	@Id
	public String getId() {
		return super.getId();
	}
}