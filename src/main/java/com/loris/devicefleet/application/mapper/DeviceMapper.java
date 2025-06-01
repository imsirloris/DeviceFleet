package com.loris.devicefleet.application.mapper;

import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.domain.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface  DeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "status", expression = "java(device.status().getDescription())")
    DeviceEntity toEntity(com.loris.devicefleet.domain.model.Device device);

    @Mapping(target = "status", expression = "java(com.loris.devicefleet.domain.model.enums.DeviceStatusEnum.fromDescription(deviceEntity.status()))")
    @Mapping(target = "createdTime", source = "createdTime", qualifiedByName = "instantToLocalDateTime")
    @Mapping(target = "updatedTime", source = "updatedTime", qualifiedByName = "instantToLocalDateTime")
    Device toDomain(DeviceEntity deviceEntity);

    @Named("instantToLocalDateTime")
    default LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }

}
