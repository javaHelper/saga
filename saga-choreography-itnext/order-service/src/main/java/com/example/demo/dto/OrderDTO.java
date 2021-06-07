package com.example.demo.dto;


import java.util.List;

import com.example.demo.enums.CookingType;
import com.example.demo.enums.OrderStatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String id;
    private String statusDescription;
    private CookingType cookingType;
    private OrderStatusType orderStatus;
    private List<HamburgerDTO> hamburgerList;
    private AddressDTO addressDTO;
    private float price;
}
