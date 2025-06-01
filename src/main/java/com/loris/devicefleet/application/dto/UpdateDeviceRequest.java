package com.loris.devicefleet.application.dto;

import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;

public record UpdateDeviceRequest(
                            String id,
                            String name,
                            String brand,
                            DeviceStatusEnum status) {
}
