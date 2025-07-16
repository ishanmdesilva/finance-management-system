package com.inova.pfms.kafka.publisher;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inova.pfms.kafka.event.IncomeEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IncomeEventPublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Async
    public void publishIncomeCreatedEvent(String userId, String incomeId, BigDecimal amount, Date date) throws JsonProcessingException {
    	IncomeEvent event = new IncomeEvent(userId, incomeId, amount, date);
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonPayload = mapper.writeValueAsString(event);
    	try {
    		kafkaTemplate.send("income.created", jsonPayload);
    	} catch (Exception e) {
    	    log.warn("Kafka server unavailable, skipping message: {}", e.getMessage());
    	}
    }
}