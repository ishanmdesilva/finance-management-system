package com.inova.pfms.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IncomeEventConsumer {

    @KafkaListener(topics = "income.created", groupId = "pfms-group", containerFactory = "incomeCreatedKafkaListenerContainerFactory")
    public void listen(String message) {
    	// Successful consumption of the message
        System.out.println("Income event consumed: " + message);
    	
		// Simulate a failure to consume the message
//    	System.out.println("Income event consume failed" + message);
//        throw new RuntimeException("Income event consume failed.");
    }
}