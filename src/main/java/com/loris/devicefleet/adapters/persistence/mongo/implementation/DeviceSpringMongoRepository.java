package com.loris.devicefleet.adapters.persistence.mongo.implementation;


import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.domain.repository.DeviceRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceSpringMongoRepository extends MongoRepository<DeviceEntity, Long>, DeviceRepository {
    List<DeviceEntity> findByBrand(String brand);
    List<DeviceEntity> findByStatus(String status);
}
