package com.example.demo.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.HamburgerDTO;

import io.swagger.annotations.ApiOperation;

public interface IKitchenApi {

	@ApiOperation(value = "View kitchen status", response = HamburgerDTO.class, responseContainer = "list")
	@RequestMapping(value = "status", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ResponseBody
	List<HamburgerDTO> status();

	@ApiOperation(value = "Add hamburger", response = Void.class)
	@RequestMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	void addHamburger(HamburgerDTO hamburgerDTO);
}