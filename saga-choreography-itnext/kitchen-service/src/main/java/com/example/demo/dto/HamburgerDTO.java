package com.example.demo.dto;

import com.example.demo.enums.HamburgerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HamburgerDTO {
	private HamburgerType hamburgerType;
	private int quantity;
}
