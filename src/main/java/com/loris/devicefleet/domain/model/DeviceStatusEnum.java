package com.loris.devicefleet.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum DeviceStatusEnum {
    AVAILABLE("Available"),
    IN_USE("In-Use"),
    INACTIVE("Inactive");

    private final String description;

    //deviceStatusEnum with lombok getter and all-args constructor
}
