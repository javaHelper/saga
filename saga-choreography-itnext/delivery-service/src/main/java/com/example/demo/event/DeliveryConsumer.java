package com.example.demo.event;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Delivery;
import com.example.demo.enums.OrderStatusType;
import com.example.demo.repository.DeliveryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeliveryConsumer {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryConsumer.class);

	@Autowired
	private DeliveryPublisher deliveryPublisher;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DeliveryRepository deliveryRepository;

	private static final String TOPIC_DELIVERY="delivery-service";

    @KafkaListener(topics = TOPIC_DELIVERY)
	public void consumeMessage(String content) {

		try {
			OrderDTO orderDTO = objectMapper.readValue(content, OrderDTO.class);
			Delivery delivery = new Delivery();
			delivery.setAddressDTO(orderDTO.getAddressDTO());
			delivery.setOrderId(orderDTO.getId());
			
			// Save Delivery Address
			deliveryRepository.save(delivery);
			
			
			logger.info("=== Processing delivery id " + delivery.getId() + " for order id " + orderDTO.getId());
			Thread.sleep(5000);
			orderDTO.setOrderStatus(OrderStatusType.DELIVERED);
			orderDTO.setStatusDescription("Delivered");
			
			
			deliveryPublisher.sendToOrderCallback(orderDTO);
			logger.info("=== Delivered order id " + orderDTO.getId());

		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}
}