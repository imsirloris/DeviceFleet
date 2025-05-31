package com.loris.devicefleet.adapters.persistence.mongo;

import com.loris.devicefleet.domain.Device;
import com.loris.devicefleet.domain.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceMongoRepository implements DeviceRepository {
    // only template at this moment -> maybe will call mapper and implementation will be on DeviceMongoRepositoryImpl
    // maybe not necessary use DeviceMongoRepositoryImpl

    @Override
    public Device save(Device device) {
        return null;
    }

    @Override
    public Optional<Device> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Device> findAll() {
        return List.of();
    }

    @Override
    public List<Device> findByBrand(String brand) {
        return List.of();
    }

    @Override
    public List<Device> findByStatus(String status) {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }
}
