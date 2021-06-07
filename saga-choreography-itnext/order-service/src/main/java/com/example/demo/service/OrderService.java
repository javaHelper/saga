package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatusType;
import com.example.demo.event.OrderServicePublisher;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderServicePublisher orderServicePublish;

	@Autowired
	private DozerBeanMapper dozer;

	public OrderDTO createOrder(OrderDTO orderDTO) {
		Order order = new Order();
		dozer.map(orderDTO, order);
		order.setOrderStatus(OrderStatusType.WAITING);
		order.setStatusDescription(OrderStatusType.WAITING.name());
		order = orderRepository.save(order);
		
		// Publish and Event
		orderServicePublish.sendOrder(order);
		logger.info("Order with id " + order.getId() + " sent to kitchen service");
		OrderDTO res = dozer.map(order, OrderDTO.class);
		return res;
	}

	public List<OrderDTO> getAll() {
		List<OrderDTO> res = new ArrayList<>();
		List<Order> orderList = orderRepository.findAll();
		if (orderList != null) {
			for (Order order : orderList)
				res.add(dozer.map(order, OrderDTO.class));
		}
		return res;
	}

	public OrderDTO getById(String id) {
		OrderDTO res = null;
		Optional<Order> order = orderRepository.findById(id);
		if (order.isPresent())
			res = dozer.map(order.get(), OrderDTO.class);
		return res;
	}
}
