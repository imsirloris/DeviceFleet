package com.loris.devicefleet.adapters.persistence.mongo;

import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.adapters.persistence.mongo.implementation.DeviceSpringMongoRepository;
import com.loris.devicefleet.application.mapper.DeviceMapper;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceMongoRepository implements DeviceRepository {

        private final DeviceSpringMongoRepository deviceSpringMongoRepository;
        private final DeviceMapper deviceMapper;

    @Override
    public DeviceEntity save(Device device) {
        return deviceSpringMongoRepository.save(deviceMapper.toEntity(device));
    }

    @Override
    public Optional<DeviceEntity> findById(String id) {
        return deviceSpringMongoRepository.findById(id);
    }

    @Override
    public Optional<List<DeviceEntity>> findAll() {
        return Optional.of(deviceSpringMongoRepository.findAll());
    }

    @Override
    public Optional<List<DeviceEntity>> findByBrand(String brand) {
        return deviceSpringMongoRepository.findByBrand(brand);
    }

    @Override
    public Optional<List<DeviceEntity>> findByStatus(String status) {
        return deviceSpringMongoRepository.findByStatus(status);
    }

    @Override
    public Optional<List<DeviceEntity>> findByBrandAndStatus(String brand, String status) {
        return deviceSpringMongoRepository.findByBrandAndStatus(brand, status);
    }

    @Override
    public DeviceEntity update(DeviceEntity device) {
        return deviceSpringMongoRepository.save(device);
    }

    @Override
    public void deleteById(String id) {
        deviceSpringMongoRepository.deleteById(id);
    }
}
