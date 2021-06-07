package com.example.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DeliveryDTO;
import com.example.demo.service.DeliveryService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/delivery/")
@Api(tags = "DeliveryServices")
public class DeliveryApi implements IDeliveryApi {

    @Autowired
    private DeliveryService deliveryService;

    @Override
    public List<DeliveryDTO> status() {
        return deliveryService.getAll();
    }
}