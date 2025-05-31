package com.loris.devicefleet.adapters.persistence.mongo;

import com.loris.devicefleet.domain.DeviceDTO;
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
    public DeviceDTO save(DeviceDTO deviceDTO) {
        return null;
    }

    @Override
    public Optional<DeviceDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DeviceDTO> findAll() {
        return List.of();
    }

    @Override
    public List<DeviceDTO> findByBrand(String brand) {
        return List.of();
    }

    @Override
    public List<DeviceDTO> findByStatus(String status) {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }
}
