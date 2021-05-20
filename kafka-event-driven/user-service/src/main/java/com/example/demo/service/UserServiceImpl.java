package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Autowired
    private UsersRepository usersRepository;
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	

	@Override
	public Long createUser(UserDto userDto) {
		User user = User.builder().firstname(userDto.getFirstname()).lastname(userDto.getLastname()).email(userDto.getEmail()).build();
		User savedUser = this.usersRepository.save(user);
		return savedUser.getId();
	}

	@Override
	@Transactional
	public void updateUser(UserDto userDto) {
		this.usersRepository.findById(userDto.getId())
			.ifPresent(user -> {
				user.setFirstname(userDto.getFirstname());
				user.setFirstname(userDto.getLastname());
				user.setEmail(userDto.getEmail());
				user.setId(userDto.getId());
				usersRepository.save(user);	
				
				// Publish Message/Event to kafka
				this.publishEvent(user);
			});
	}


	private void publishEvent(User user) {
		try {
			String value = objectMapper().writeValueAsString(user);
			kafkaTemplate.sendDefault(user.getId(), value);
		} catch (Exception e) {
			log.error("UserServiceImpl | publishEvent | Error {}", e.getMessage());
		}
	}


	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.usersRepository.findAll();
		return users.stream().map(u -> UserDto.builder()
				.firstname(u.getFirstname())
				.lastname(u.getLastname())
				.email(u.getEmail())
				.id(u.getId())
				.build())
			.collect(Collectors.toList());
	}
}
