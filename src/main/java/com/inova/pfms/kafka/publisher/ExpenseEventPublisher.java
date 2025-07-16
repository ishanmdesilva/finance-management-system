package com.inova.pfms.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inova.pfms.kafka.event.ExpenseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class ExpenseEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ExpenseEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void publishExpenseCreatedEvent(String userId, String expenseId, String expenseCategoryId, BigDecimal amount, Date date) throws JsonProcessingException {
        ExpenseEvent event = new ExpenseEvent(userId, expenseId, expenseCategoryId, amount, date);
        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(event);
        try {
            kafkaTemplate.send("expense.created", jsonPayload);
        } catch (Exception e) {
            log.warn("Kafka unavailable, skipping message: {}", e.getMessage());
        }
    }

}
