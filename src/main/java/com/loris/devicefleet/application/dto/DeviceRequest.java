package com.loris.devicefleet.application.dto;

import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;

public record DeviceRequest(
                            String id,
                            String name,
                            String brand,
                            DeviceStatusEnum status) {
}
