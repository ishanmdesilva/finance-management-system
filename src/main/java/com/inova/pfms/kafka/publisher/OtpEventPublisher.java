package com.inova.pfms.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inova.pfms.kafka.event.OtpEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class OtpEventPublisher {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Async
    public void createOtpEvent(String otpId, String otp, String email, Date date) throws JsonProcessingException {
        OtpEvent event = new OtpEvent(otpId, otp, email, date);
        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(event);
        try {
            kafkaTemplate.send("otp.created", jsonPayload);
        } catch (Exception e) {
            log.warn("Kafka unavailable, skipping message: {}", e.getMessage());
            throw e;
        }
    }

}
