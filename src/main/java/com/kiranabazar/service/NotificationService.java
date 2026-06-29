package com.kiranabazar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendEmail(String email, Long orderId) {
        log.info("Sending order confirmation email to {} for order {}", email, orderId);
    }

    public void sendSms(String phoneNumber, Long orderId) {
        log.info("Sending SMS notification to {} for order {}", phoneNumber, orderId);
    }
}
