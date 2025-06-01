package com.loris.devicefleet.application.service;

import com.loris.devicefleet.adapters.persistence.mongo.DeviceMongoRepository;
import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.application.mapper.DeviceMapper;
import com.loris.devicefleet.domain.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceMongoRepository repository;
    private final DeviceMapper mapper;

    public Device createDevice(Device device) {
        log.info("Creating new device: {} ", device);

        DeviceEntity savedDevice = repository.save(device);

        log.info("Device saved: {}", savedDevice);
        return mapper.toDomain(savedDevice);
    }
}
