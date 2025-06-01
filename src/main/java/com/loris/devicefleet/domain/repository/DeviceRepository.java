package com.loris.devicefleet.domain.repository;

import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.domain.model.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    // Methods to interact with the device repository
    DeviceEntity save(Device device);

    Optional<DeviceEntity> findById(Long id);

    List<DeviceEntity> findAll();

    List<DeviceEntity> findByBrand(String brand);

    List<DeviceEntity> findByStatus(String status);

    void deleteById(Long id);
}
