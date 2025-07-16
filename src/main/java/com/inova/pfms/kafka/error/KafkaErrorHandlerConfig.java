package com.inova.pfms.kafka.error;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
//import org.apache.kafka.common.TopicPartition;
//import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
//import org.springframework.kafka.listener.DefaultErrorHandler;
//import org.springframework.util.backoff.FixedBackOff;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;


@Configuration
public class KafkaErrorHandlerConfig {
	
	@Bean
	ConcurrentKafkaListenerContainerFactory<String, String> incomeCreatedKafkaListenerContainerFactory(
			ConsumerFactory<String, String> consumerFactory, KafkaTemplate<String, String> kafkaTemplate) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
				(record, ex) -> new TopicPartition("income.created.errors", 0));//move to config

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3));//move to config

		errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
			System.out.println("Retry attempt " + deliveryAttempt + " for message: " + record.value());
		});

		factory.setCommonErrorHandler(errorHandler);

		return factory;
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, String> expenseCreatedKafkaListenerContainerFactory(
			ConsumerFactory<String, String> consumerFactory, KafkaTemplate<String, String> kafkaTemplate) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
				(record, ex) -> new TopicPartition("expense.created.errors", 0));//move to config

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3));//move to config

		errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
			System.out.println("Retry attempt " + deliveryAttempt + " for message: " + record.value());
		});

		factory.setCommonErrorHandler(errorHandler);

		return factory;
	}

}
