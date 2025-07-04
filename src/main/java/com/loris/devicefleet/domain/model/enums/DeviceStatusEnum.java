package com.loris.devicefleet.domain.model.enums;

import lombok.Getter;

@Getter
public enum DeviceStatusEnum {
    AVAILABLE("AVAILABLE"),
    IN_USE("IN-USE"),
    INACTIVE("INACTIVE");

    private final String description;

    DeviceStatusEnum(String description) {
        this.description = description;
    }

    public static DeviceStatusEnum fromDescription(String description) {
        for (DeviceStatusEnum status : DeviceStatusEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching DeviceStatusEnum for description: " + description);
    }
}
