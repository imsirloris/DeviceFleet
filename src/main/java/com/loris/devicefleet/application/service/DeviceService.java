package com.loris.devicefleet.application.service;

import com.loris.devicefleet.adapters.persistence.mongo.DeviceMongoRepository;
import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.application.dto.CreateDeviceRequest;
import com.loris.devicefleet.application.dto.UpdateDeviceRequest;
import com.loris.devicefleet.application.dto.ApiMessage;
import com.loris.devicefleet.application.mapper.DeviceMapper;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceMongoRepository repository;
    private final DeviceMapper mapper;

    @Transactional
    public ResponseEntity<Device> createDevice(CreateDeviceRequest device) {
        log.info("Creating new device: {} ", device);

        DeviceStatusEnum status = device.status() == null ? DeviceStatusEnum.AVAILABLE : device.status();

        DeviceEntity savedDevice = repository.save(new Device(null, device.name(), device.brand(), status, null, null));
        log.info("Device created successfully: {}", savedDevice);
        return ResponseEntity.ok(mapper.toDomain(savedDevice));
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public ResponseEntity<?> getDeviceById(String id) {
        log.info("Fetching device by ID: {}", id);

        Optional<DeviceEntity> deviceEntity = repository.findById(id);

        log.info("Device found: {}", deviceEntity);

        if (deviceEntity.isEmpty()) {
            log.warn("Device with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiMessage<>("Device not found"));
        }

        Device device = mapper.toDomain(deviceEntity.get());

        return ResponseEntity.ok(device);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getDevicesByBrandOrStatus(String brand, DeviceStatusEnum status) {
        log.info("Fetching devices by brand: {} or status: {}", brand, status);

        Optional<List<DeviceEntity>> deviceEntities;

        // Check if both brand and status are provided
        if (ObjectUtils.isNotEmpty(brand) && ObjectUtils.isNotEmpty(status)) {
            deviceEntities = repository.findByBrandAndStatus(brand, status.name());
        } else if (ObjectUtils.isNotEmpty(brand)) {
            deviceEntities = repository.findByBrand(brand);
        } else if (ObjectUtils.isNotEmpty(status)) {
            deviceEntities = repository.findByStatus(status.name());
        } else {
            log.warn("Both brand and status are empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiMessage<>("Brand or status must be provided"));
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

    @Transactional
    public ResponseEntity<?> updateDevice(UpdateDeviceRequest updateDeviceRequest) {
        log.info("Updating deviceRequest: {}", updateDeviceRequest);

        //check if the deviceRequest exists
        Optional<DeviceEntity> existingDevice = repository.findById(updateDeviceRequest.id());

        if (existingDevice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiMessage<>("Device not found with ID: " + updateDeviceRequest.id() ));
        }

        if(existingDevice.get().status().equals(DeviceStatusEnum.IN_USE.getDescription())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiMessage<>("Device is currently in use and cannot be updated"));
        }

        // Update the deviceRequest fields
        DeviceEntity updateDevice = new DeviceEntity(
                existingDevice.get().id(),
                updateDeviceRequest.name() != null ? updateDeviceRequest.name() : existingDevice.get().name(),
                updateDeviceRequest.brand() != null ? updateDeviceRequest.brand() : existingDevice.get().brand(),
                updateDeviceRequest.status() != null ? updateDeviceRequest.status().getDescription() : existingDevice.get().status(),
                existingDevice.get().createdTime(),
                null
        );

        DeviceEntity updatedDevice = repository.update(updateDevice);
        log.info("Device updated successfully: {}", updatedDevice);

        return ResponseEntity.ok(mapper.toDomain(updatedDevice));
    }

    @Transactional
    public ResponseEntity<?> updateDeviceStatus(String id, DeviceStatusEnum status) {
        log.info("Updating device status for ID: {} to {}", id, status);

        // Check if the device exists
        Optional<DeviceEntity> existingDevice = repository.findById(id);

        if (existingDevice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiMessage<>("Device not found with ID: " + id));
        }

        // Update the device status
        DeviceEntity updatedDevice = new DeviceEntity(
                existingDevice.get().id(),
                existingDevice.get().name(),
                existingDevice.get().brand(),
                status.getDescription(),
                existingDevice.get().createdTime(),
                null
        );

        DeviceEntity savedDevice = repository.update(updatedDevice);
        log.info("Device status updated successfully: {}", savedDevice);

        return ResponseEntity.ok(mapper.toDomain(savedDevice));
    }

    @Transactional
    public ResponseEntity<?> deleteDevice(String id) {
        log.info("Deleting device with ID: {}", id);

        // Check if the device exists
        Optional<DeviceEntity> existingDevice = repository.findById(id);

        if (existingDevice.isEmpty()) {
            log.warn("Device with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiMessage<>("Device not found"));
        }

        // Check if the device is in use
        if (existingDevice.get().status().equals(DeviceStatusEnum.IN_USE.getDescription())) {
            log.warn("Device with ID {} is currently in use and cannot be deleted", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiMessage<>("Device is currently in use and cannot be deleted"));
        }

        repository.deleteById(id);

        log.info("Device with ID {} deleted successfully", id);

        return ResponseEntity.ok(new ApiMessage<>("Device deleted successfully"));
    }
}
