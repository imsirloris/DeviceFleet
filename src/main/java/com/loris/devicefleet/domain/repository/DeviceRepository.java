package com.loris.devicefleet.domain.repository;

import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.domain.model.Device;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    // Methods to interact with the device repository
    DeviceEntity save(Device device);

    Optional<DeviceEntity> findById(String id);

    Optional<List<DeviceEntity>> findAll();

    Optional<List<DeviceEntity>> findByBrand(String brand);

    Optional<List<DeviceEntity>> findByStatus(String status);

    Optional<List<DeviceEntity>> findByBrandAndStatus(String brand, String status);

    void deleteById(Long id);
}
