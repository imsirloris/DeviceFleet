package com.loris.devicefleet.adapters.persistence.mongo.implementation;


import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceMongoRepositoryImpl extends MongoRepository<DeviceEntity, String> {
}
