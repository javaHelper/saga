package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.dto.PaymentRequestDTO;
import com.example.dto.PaymentResponseDTO;
import com.example.enums.PaymentStatus;

@Service
public class PaymentService {

	private Map<Integer, Double> userBalanceMap;

	@PostConstruct
	private void init() {
		this.userBalanceMap = new HashMap<>();
		this.userBalanceMap.put(1, 1000d);
		this.userBalanceMap.put(2, 1000d);
		this.userBalanceMap.put(3, 1000d);
	}

	public PaymentResponseDTO debit(final PaymentRequestDTO requestDTO) {
		double balance = this.userBalanceMap.getOrDefault(requestDTO.getUserId(), 0d);

		PaymentResponseDTO responseDTO = new PaymentResponseDTO();
		responseDTO.setAmount(requestDTO.getAmount());
		responseDTO.setUserId(requestDTO.getUserId());
		responseDTO.setOrderId(requestDTO.getOrderId());
		responseDTO.setStatus(PaymentStatus.PAYMENT_REJECTED);

		if (balance >= requestDTO.getAmount()) {
			responseDTO.setStatus(PaymentStatus.PAYMENT_APPROVED);
			this.userBalanceMap.put(requestDTO.getUserId(), balance - requestDTO.getAmount());
		}
		return responseDTO;
	}

	public void credit(final PaymentRequestDTO requestDTO) {
		this.userBalanceMap.computeIfPresent(requestDTO.getUserId(), (k, v) -> v + requestDTO.getAmount());
	}

}