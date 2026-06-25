package com.ecommerce.common.outbox;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class OutboxEvent {

    @Id
    private String id;

    private String topic;

    @Column(name = "event_key")
    private String key;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private String createdAt;

    public OutboxEvent(String topic, String key, String payload, String createdAt) {
        this.id = UUID.randomUUID().toString();
        this.topic = topic;
        this.key = key;
        this.payload = payload;
        this.status = OutboxStatus.PENDING;
        this.createdAt = createdAt;
    }
}
