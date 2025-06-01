package com.loris.devicefleet.application.dto;

import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;

public record CreateDeviceRequest(
        String name,
        String brand,
        DeviceStatusEnum status) {
}
