package com.loris.devicefleet.domain.repository;

import com.loris.devicefleet.domain.DeviceDTO;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    // Methods to interact with the device repository
    DeviceDTO save(DeviceDTO deviceDTO);

    Optional<DeviceDTO> findById(Long id);

    List<DeviceDTO> findAll();

    List<DeviceDTO> findByBrand(String brand);

    List<DeviceDTO> findByStatus(String status);

    void deleteById(Long id);


}
