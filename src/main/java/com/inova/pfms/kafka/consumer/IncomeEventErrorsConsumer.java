package com.inova.pfms.kafka.consumer;

import com.inova.pfms.constants.LogMessages;
import com.inova.pfms.service.DeadLetterEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.inova.pfms.constants.LogMessages.CONSUMED_DEAD_LETTER_INCOME;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class IncomeEventErrorsConsumer {

    private final DeadLetterEventService deadLetterEventService;

    @KafkaListener(topics = "income.created.errors", groupId = "pfms-group")
    public void listen(String message) {
        log.info(CONSUMED_DEAD_LETTER_INCOME, message);
        deadLetterEventService.handleIncomeEventError(message);
    }
}