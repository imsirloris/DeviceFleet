package com.loris.devicefleet.domain;

import com.loris.devicefleet.domain.model.Enums.DeviceStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class DeviceDTO {
    private String id;
    private String name;
    private String brand;
    private DeviceStatusEnum status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
