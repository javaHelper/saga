package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.UserDto;

public interface UserService {
	Long createUser(UserDto userDto);

	void updateUser(UserDto userDto);

	List<UserDto> getAllUsers();
}
