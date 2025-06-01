package com.loris.devicefleet.application.service;

import com.loris.devicefleet.adapters.persistence.mongo.DeviceMongoRepository;
import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.application.dto.response.ApiResponse;
import com.loris.devicefleet.application.mapper.DeviceMapper;
import com.loris.devicefleet.domain.model.Device;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceMongoRepository repository;
    private final DeviceMapper mapper;

    public ResponseEntity<Device> createDevice(Device device) {
        log.info("Creating new device: {} ", device);
        DeviceEntity savedDevice = repository.save(device);
        log.info("Device created successfully: {}", savedDevice);
        return ResponseEntity.ok(mapper.toDomain(savedDevice));
    }

    public ResponseEntity<?> getAllDevices() {
        log.info("Fetching all devices");

        Optional<List<DeviceEntity>> deviceEntities = repository.findAll();

        if (deviceEntities.isEmpty()) {
            log.warn("No devices found");
            return ResponseEntity.noContent().build();
        }

        List<Device> devices = deviceEntities.get().stream()
                .map(mapper::toDomain)
                .toList();

        return ResponseEntity.ok(devices);
    }

    public ResponseEntity<?> getDeviceById(String id) {
        log.info("Fetching device by ID: {}", id);

        Optional<DeviceEntity> deviceEntity = repository.findById(id);

        log.info("Device found: {}", deviceEntity);

        if (deviceEntity.isEmpty()) {
            log.warn("Device with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Device not found"));
        }

        Device device = mapper.toDomain(deviceEntity.get());

        return ResponseEntity.ok(device);
    }

    public ResponseEntity<?> getDevicesByBrandOrStatus(String brand, String status) {
        log.info("Fetching devices by brand: {} or status: {}", brand, status);

        // Check if both brand and status are null or empty

        Optional<List<DeviceEntity>> deviceEntities;

        if (ObjectUtils.isNotEmpty(brand) && ObjectUtils.isNotEmpty(status)) {
            deviceEntities = repository.findByBrandAndStatus(brand, status);
        } else if (ObjectUtils.isNotEmpty(brand)) {
            deviceEntities = repository.findByBrand(brand);
        } else if (ObjectUtils.isNotEmpty(status)) {
            deviceEntities = repository.findByStatus(status);
        } else {
            log.warn("Both brand and status are empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Brand or status must be provided"));
        }

        if (deviceEntities.isEmpty() || deviceEntities.get().isEmpty()) {
            log.warn("No devices found for brand: {} or status: {}", brand, status);
            return ResponseEntity.noContent().build();
        }

        List<Device> devices = deviceEntities.get().stream()
                .map(mapper::toDomain)
                .toList();

        return ResponseEntity.ok(devices);
    }
}
