package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user-service")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public Long createUser(@RequestBody UserDto userDto) {
		return this.userService.createUser(userDto);
	}

	@PutMapping("/update")
	public void updateUser(@RequestBody UserDto userDto) {
		this.userService.updateUser(userDto);
	}

	@GetMapping
	public List<UserDto> getAllUsers(){
		return this.userService.getAllUsers();
	}
}