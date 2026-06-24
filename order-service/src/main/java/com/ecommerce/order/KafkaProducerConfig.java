package com.ecommerce.order;

import com.ecommerce.common.event.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {

    /*
     * Topic'i kod tarafında tanımlıyoruz.
     * Eski yöntem: Topic'ler Kafka'da elle oluşturulurdu (kafka-topics.sh scripti ile).
     * Yeni yöntem: Spring Boot uygulama ayağa kalkarken topic yoksa otomatik oluşturuyor.
     *
     * partitions(3)  → 3 partition — aynı anda 3 consumer paralel okuyabilir
     * replicas(1)    → 1 replica — local ortamda tek broker olduğu için 1 yeterli,
     *                  prodüksiyonda en az 3 olur
     */
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
