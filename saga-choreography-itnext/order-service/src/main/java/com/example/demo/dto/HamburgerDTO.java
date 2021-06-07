package com.example.demo.dto;

import com.example.demo.enums.HamburgerType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HamburgerDTO {
	private HamburgerType hamburgerType;
	private int quantity;
}
