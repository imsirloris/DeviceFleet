package com.loris.devicefleet.domain.model.Enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceStatusEnum {
    AVAILABLE("Available"),
    IN_USE("In-Use"),
    INACTIVE("Inactive");

    private final String description;

    //deviceStatusEnum
}
