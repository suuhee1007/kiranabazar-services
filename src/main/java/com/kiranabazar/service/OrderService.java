package com.kiranabazar.service;

import com.kiranabazar.dto.OrderItemRequest;
import com.kiranabazar.dto.OrderRequest;
import com.kiranabazar.model.Order;
import com.kiranabazar.model.OrderItem;
import com.kiranabazar.model.Product;
import com.kiranabazar.model.User;
import com.kiranabazar.repository.OrderRepository;
import com.kiranabazar.repository.ProductRepository;
import com.kiranabazar.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Order createOrder(String username, OrderRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Registered user not found"));

        List<OrderItem> items = request.getItems().stream()
                .map(this::buildItem)
                .collect(Collectors.toList());

        double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUsername(username);
        order.setStatus("PAID");
        order.setCreatedAt(Instant.now());
        order.setShippingAddress(request.getShippingAddress());
        order.setTotalAmount(totalAmount);
        order.setItems(items);

        Order saved = orderRepository.save(order);
        notificationService.sendEmail(user.getEmail(), saved.getId());
        notificationService.sendSms(user.getPhoneNumber(), saved.getId());
        return saved;
    }

    private OrderItem buildItem(OrderItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + request.getProductId()));

        return new OrderItem(product.getId(), product.getName(), product.getPrice(), request.getQuantity());
    }
}
