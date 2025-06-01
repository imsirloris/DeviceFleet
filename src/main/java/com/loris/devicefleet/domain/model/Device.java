package com.loris.devicefleet.domain.model;

import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public record Device(
        String id,
        String name,
        String brand,
        DeviceStatusEnum status,
        LocalDateTime createdTime,
        LocalDateTime updatedTime) {
}