package com.example.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderDTO;
import com.example.demo.service.OrderService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/order/")
@Api(tags = "OrderServices")
public class OrderApi implements IOrderServiceApi {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO create(OrderDTO request) {
        return orderService.createOrder(request);
    }

    @Override
    public OrderDTO view(String id) {
        return orderService.getById(id);
    }

    @Override
    public List<OrderDTO> viewAll() {
        return orderService.getAll();
    }
}