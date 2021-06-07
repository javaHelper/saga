package com.example.demo.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.DeliveryDTO;

import io.swagger.annotations.ApiOperation;


public interface IDeliveryApi {


    @ApiOperation(value = "View delivery status", response = DeliveryDTO.class,responseContainer = "list")
    @RequestMapping(value = "status", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    List<DeliveryDTO> status();
}
