package com.example.userservice.query.rest;

import com.example.core.model.User;
import com.example.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{userId}/payment-details")
    public User getUserPaymentDetails(@PathVariable String userId) {
        FetchUserPaymentDetailsQuery query = new FetchUserPaymentDetailsQuery(userId);
 
        return queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();

    }
}