package com.inova.pfms.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inova.pfms.kafka.event.BudgetAlertEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BudgetEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public BudgetEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void publishBudgetAlertEvent(String userId, String budgetId, String expenseCategoryId, BigDecimal amount) throws JsonProcessingException {
        BudgetAlertEvent event = new BudgetAlertEvent(userId, budgetId, expenseCategoryId, amount);
        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(event);
        try {
            kafkaTemplate.send("budget.alert", jsonPayload);
        } catch (Exception e) {
            log.warn("Kafka unavailable, skipping message: {}", e.getMessage());
        }
    }
}
