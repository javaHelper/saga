package com.example.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.HamburgerDTO;
import com.example.demo.service.KitchenService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/kitchen/")
@Api(tags = "KitchenServices")
public class KitchenApi implements IKitchenApi {

	@Autowired
	private KitchenService kitchenService;

	@Override
	public List<HamburgerDTO> status() {
		return kitchenService.getStatus();
	}

	@Override
	public void addHamburger(HamburgerDTO hamburgerDTO) {
		kitchenService.addHamburger(hamburgerDTO);
	}
}