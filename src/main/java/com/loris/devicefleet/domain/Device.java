package com.loris.devicefleet.domain;

import com.loris.devicefleet.domain.model.Enums.DeviceStatusEnum;

import java.time.LocalDateTime;

public record Device(
        Long id,
        String name,
        String brand,
        DeviceStatusEnum status,
        LocalDateTime createdTime,
        LocalDateTime updatedTime) {
}