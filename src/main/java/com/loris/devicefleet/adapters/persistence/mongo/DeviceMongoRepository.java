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
    public Optional<DeviceEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DeviceEntity> findAll() {
        return List.of();
    }

    @Override
    public List<DeviceEntity> findByBrand(String brand) {
        return List.of();
    }

    @Override
    public List<DeviceEntity> findByStatus(String status) {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }
}
