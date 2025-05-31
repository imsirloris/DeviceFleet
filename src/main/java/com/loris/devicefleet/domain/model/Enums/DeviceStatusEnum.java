package com.loris.devicefleet.domain.model.Enums;

import lombok.Getter;

@Getter
public enum DeviceStatusEnum {
    AVAILABLE("Available"),
    IN_USE("In-Use"),
    INACTIVE("Inactive");

    private final String description;

    DeviceStatusEnum(String description) {
        this.description = description;
    }
}
