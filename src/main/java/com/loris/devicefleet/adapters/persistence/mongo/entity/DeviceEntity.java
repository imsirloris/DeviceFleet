package com.loris.devicefleet.adapters.persistence.mongo.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "devices")
public record DeviceEntity(
        @Id
        ObjectId id,
        String name,
        String brand,
        String status,
        @CreatedDate Instant createdTime,
        @LastModifiedDate Instant updatedTime) {
        //mongodb entity for device
}
