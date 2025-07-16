package com.inova.pfms.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OtpEventConsumer {

    @KafkaListener(topics = "otp.created", groupId = "pfms-group")
    public void sendEmailListener(String otpEvent) {
        System.out.println("Generated OTP : " + otpEvent);
    }
}
