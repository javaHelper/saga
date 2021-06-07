package com.example.demo.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.OrderDTO;

import io.swagger.annotations.ApiOperation;

public interface IOrderServiceApi {

    @ApiOperation(value = "Create order", response = OrderDTO.class)
    @RequestMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    OrderDTO create(@RequestBody OrderDTO request);

    @ApiOperation(value = "View order", response = OrderDTO.class)
    @RequestMapping(value = "view/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody OrderDTO view(@PathVariable String id);

    @ApiOperation(value = "All orders", response = OrderDTO.class,responseContainer = "list")
    @RequestMapping(value = "view", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    List<OrderDTO> viewAll();
}