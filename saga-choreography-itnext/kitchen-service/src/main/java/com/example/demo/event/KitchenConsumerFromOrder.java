package com.example.demo.event;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.enums.OrderStatusType;
import com.example.demo.service.KitchenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KitchenConsumerFromOrder {

	private static final Logger logger = LoggerFactory.getLogger(KitchenConsumerFromOrder.class);

	@Autowired
	private KitchenService kitchenService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KithcenPublisher kithcenPublisher;

	private static final String ORDER_TOPIC="order-service";

    @KafkaListener(topics = ORDER_TOPIC)
	public void consumeMessage(String content) {

		try {
			OrderDTO orderDTO = objectMapper.readValue(content, OrderDTO.class);

			boolean started = kitchenService.process(orderDTO);
			kithcenPublisher.sendToOrderCallback(orderDTO);

			if (started) {
				logger.info("Start cooking for order id " + orderDTO.getId() + " start");
				Thread.sleep(5000);
				logger.info("Packaging start");
				orderDTO.setOrderStatus(OrderStatusType.PACKAGING);
				orderDTO.setStatusDescription("Order in packaging");

				kithcenPublisher.sendToOrderCallback(orderDTO);
				
				
				logger.info("Callback to order service sent");
				kithcenPublisher.sendToDelivery(orderDTO);
				logger.info("Order id " + orderDTO.getId() + " sent to delivery");
			}
		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
