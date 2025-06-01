package com.loris.devicefleet.adapters.persistence.mongo.implementation;


import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceSpringMongoRepository extends MongoRepository<DeviceEntity, String> {
    Optional<List<DeviceEntity>> findByBrand(String brand);
    Optional<List<DeviceEntity>> findByStatus(String status);
    Optional<List<DeviceEntity>> findByBrandAndStatus(String brand, String status);
}
