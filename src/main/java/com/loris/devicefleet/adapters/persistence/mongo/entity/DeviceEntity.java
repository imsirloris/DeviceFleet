package com.loris.devicefleet.adapters.persistence.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DeviceEntity {
    //mongodb entity for device

    @Id
    String id;

    String name;

    String brand;

    String status;

    String createdTime;

    String updatedTime;
}
