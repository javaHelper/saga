package com.example.demo.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KithcenPublisher {

	private final static String TOPIC_ORDER_CALLBACK = "orderservicecallback";

	private final static String TOPIC_DELIVERY = "delivery-service";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void sendToOrderCallback(OrderDTO orderDTO) throws JsonProcessingException {
		kafkaTemplate.send(TOPIC_ORDER_CALLBACK, objectMapper.writeValueAsString(orderDTO));
	}

	public void sendToDelivery(OrderDTO orderDTO) throws JsonProcessingException {
		kafkaTemplate.send(TOPIC_DELIVERY, objectMapper.writeValueAsString(orderDTO));
	}
}