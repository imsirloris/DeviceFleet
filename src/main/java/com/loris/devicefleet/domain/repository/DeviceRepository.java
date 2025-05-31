package com.loris.devicefleet.domain.repository;

import com.loris.devicefleet.domain.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    // Methods to interact with the device repository
    Device save(Device device);

    Optional<Device> findById(Long id);

    List<Device> findAll();

    List<Device> findByBrand(String brand);

    List<Device> findByStatus(String status);

    void deleteById(Long id);
}
