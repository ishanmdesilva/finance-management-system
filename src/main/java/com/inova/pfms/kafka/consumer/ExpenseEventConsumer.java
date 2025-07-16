package com.inova.pfms.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ExpenseEventConsumer {

    @KafkaListener(topics = "expense.created", groupId = "pfms-group", containerFactory = "expenseCreatedKafkaListenerContainerFactory")
    public void listen(String message) {
        System.out.println("Expense event consumed: " + message);
    }

}
