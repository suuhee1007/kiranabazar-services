package com.kiranabazar.controller;

import com.kiranabazar.dto.OrderRequest;
import com.kiranabazar.dto.OrderResponse;
import com.kiranabazar.model.Order;
import com.kiranabazar.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(Authentication authentication, @RequestBody OrderRequest request) {
        String username = authentication.getName();
        Order order = orderService.createOrder(username, request);
        return ResponseEntity.ok(new OrderResponse(order.getId(), "Order placed successfully."));
    }
}
