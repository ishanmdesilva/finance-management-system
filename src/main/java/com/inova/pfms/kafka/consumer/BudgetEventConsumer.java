package com.inova.pfms.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BudgetEventConsumer {

    @KafkaListener(topics = "budget.alert", groupId = "pfms-group")
    public void listen(String message) {
        System.out.println("Budget alert event consumed: " + message);
    }

}
