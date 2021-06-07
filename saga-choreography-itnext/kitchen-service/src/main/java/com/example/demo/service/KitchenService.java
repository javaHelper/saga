package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.HamburgerDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Hamburger;
import com.example.demo.enums.OrderStatusType;
import com.example.demo.repository.HamburgerRepository;

@Service
public class KitchenService {

	private static final Logger logger = LoggerFactory.getLogger(KitchenService.class);

	@Autowired
	private HamburgerRepository hamburgerRepository;

	public void addHamburger(HamburgerDTO hamburgerDTO) {
		hamburgerRepository.save(Hamburger.builder().hamburgerType(hamburgerDTO.getHamburgerType()).build());
	}

	public List<HamburgerDTO> getStatus() {
		List<HamburgerDTO> list = new ArrayList<>();
		List<Hamburger> hamburgerList = hamburgerRepository.findAll();
		
		return hamburgerList.stream().map(e -> HamburgerDTO.builder()
				.hamburgerType(e.getHamburgerType())
				.build())
				.collect(Collectors.toList());

//		if (hamburgerList != null) {
//			Map<HamburgerType, Integer> hamburgerTypeSizeMap = new HashMap<>();
//
//			for (Hamburger hamburger : hamburgerList) {
//				Integer currentSize = null;
//				if ((currentSize = hamburgerTypeSizeMap.get(hamburger.getHamburgerType())) != null)
//					currentSize = currentSize.intValue() + 1;
//				else
//					currentSize = 1;
//				hamburgerTypeSizeMap.put(hamburger.getHamburgerType(), currentSize);
//			}
//			for (Map.Entry<HamburgerType, Integer> entry : hamburgerTypeSizeMap.entrySet())
//				list.add(new HamburgerDTO(entry.getKey(), entry.getValue()));
//		}
//		return list;
	}

	public synchronized boolean process(OrderDTO orderDTO) {
		List<HamburgerDTO> hamburgerDTOList = orderDTO.getHamburgerList();
		List<Hamburger> candidatesHamburger = new ArrayList<>();
		for (HamburgerDTO hDto : hamburgerDTOList) {
			List<Hamburger> hamburgerList = hamburgerRepository.findByHamburgerType(hDto.getHamburgerType());
			if (hamburgerList != null && hamburgerList.size() >= hDto.getQuantity()) {
				int i = 0;
				for (Hamburger hamburger : hamburgerList) {
					++i;
					candidatesHamburger.add(hamburger);
					if (i == hDto.getQuantity())
						break;

				}
			} else {
				orderDTO.setOrderStatus(OrderStatusType.ABORTED);
				orderDTO.setStatusDescription(hDto.getHamburgerType().getDescription() + " finished, only "
						+ hamburgerList.size() + " in the fridge");
				logger.info("Order aborted");
				return false;
			}
		}
		orderDTO.setOrderStatus(OrderStatusType.COOKING);
		orderDTO.setStatusDescription("Order in cooking");
		for (Hamburger hamburger : candidatesHamburger) {
			hamburgerRepository.deleteById(hamburger.getId());
		}
		return true;
	}
}
