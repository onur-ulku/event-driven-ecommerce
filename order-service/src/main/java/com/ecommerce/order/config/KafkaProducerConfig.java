package com.ecommerce.order.config;

import com.ecommerce.common.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.ORDER_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deadLetterTopic() {
        return TopicBuilder.name(KafkaTopics.DEAD_LETTER_QUEUE)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
