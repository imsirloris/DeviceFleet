package com.loris.devicefleet.application.service;

import com.loris.devicefleet.adapters.persistence.mongo.DeviceMongoRepository;
import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.application.dto.ApiMessage;
import com.loris.devicefleet.application.dto.CreateDeviceRequest;
import com.loris.devicefleet.application.dto.UpdateDeviceRequest;
import com.loris.devicefleet.application.mapper.DeviceMapper;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceMongoRepository repository;

    @Mock
    private DeviceMapper mapper;

    @InjectMocks
    private DeviceService deviceService;

    private static final String DEVICE_ID = "683c817f1d1ca53dfe24c1df";
    private static final String DEVICE_NAME = "iPhone 14";
    private static final String DEVICE_BRAND = "Apple";
    private static final DeviceStatusEnum DEVICE_STATUS = DeviceStatusEnum.AVAILABLE;
    private static final Instant CREATED_TIME = Instant.parse("2025-06-01T16:36:15.255Z");
    private static final Instant UPDATED_TIME = Instant.parse("2025-06-01T16:36:15.255Z");
    private static final LocalDateTime LOCAL_CREATED_TIME = LocalDateTime.parse("2025-06-01T16:36:15.255");
    private static final LocalDateTime LOCAL_UPDATED_TIME = LocalDateTime.parse("2025-06-01T16:36:15.255");

    @Test
    @DisplayName("Should create a device successfully")
    void shouldCreateDeviceSuccessfully() {
        CreateDeviceRequest request = new CreateDeviceRequest(DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS);
        DeviceEntity entity = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS.getDescription(), CREATED_TIME, UPDATED_TIME);
        Device device = new Device(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS, LOCAL_CREATED_TIME, LOCAL_UPDATED_TIME);

        when(repository.save(any())).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(device);

        ResponseEntity<Device> response = deviceService.createDevice(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(device, response.getBody());
        verify(repository).save(any());
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should get all devices successfully")
    void shouldGetAllDevicesSuccessfully() {
        DeviceEntity entity = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS.getDescription(), CREATED_TIME, UPDATED_TIME);
        Device device = new Device(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS, LOCAL_CREATED_TIME, LOCAL_UPDATED_TIME);
        List<DeviceEntity> entities = List.of(entity);

        when(repository.findAll()).thenReturn(Optional.of(entities));
        when(mapper.toDomain(entity)).thenReturn(device);

        ResponseEntity<?> response = deviceService.getAllDevices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(device), response.getBody());
        verify(repository).findAll();
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return no content when no devices found")
    void shouldReturnNoContentWhenNoDevicesFound() {
        when(repository.findAll()).thenReturn(Optional.empty());

        ResponseEntity<?> response = deviceService.getAllDevices();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(repository).findAll();
        verifyNoInteractions(mapper);
    }

    @ParameterizedTest
    @MethodSource("getDeviceByIdTestCases")
    @DisplayName("Should get device by id with different scenarios")
    void shouldGetDeviceById(String id, Optional<DeviceEntity> entityOptional, HttpStatus expectedStatus, Object expectedBody) {
        when(repository.findById(id)).thenReturn(entityOptional);

        if (entityOptional.isPresent()) {
            DeviceEntity entity = entityOptional.get();
            Device device = new Device(entity.id(), entity.name(), entity.brand(), DeviceStatusEnum.valueOf(entity.status()), LOCAL_CREATED_TIME, LOCAL_UPDATED_TIME);
            when(mapper.toDomain(entity)).thenReturn(device);
        }

        ResponseEntity<?> response = deviceService.getDeviceById(id);

        assertEquals(expectedStatus, response.getStatusCode());
        if (expectedBody instanceof Device) {
            assertEquals(expectedBody, response.getBody());
        } else if (expectedBody instanceof ApiMessage) {
            assertInstanceOf(ApiMessage.class, response.getBody());
        }
    }

    static Stream<Arguments> getDeviceByIdTestCases() {
        DeviceEntity entity = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS.name(), CREATED_TIME, UPDATED_TIME);
        Device device = new Device(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS, LOCAL_CREATED_TIME, LOCAL_UPDATED_TIME);

        return Stream.of(
                Arguments.of(DEVICE_ID, Optional.of(entity), HttpStatus.OK, device),
                Arguments.of("non-existent-id", Optional.empty(), HttpStatus.NOT_FOUND, new ApiMessage<>("Device not found"))
        );
    }

    @ParameterizedTest
    @MethodSource("getDevicesByBrandOrStatusTestCases")
    @DisplayName("Should get devices by brand or status with different scenarios")
    void shouldGetDevicesByBrandOrStatus(String brand, DeviceStatusEnum status, Optional<List<DeviceEntity>> entities, HttpStatus expectedStatus) {
        if (brand != null && status != null) {
            when(repository.findByBrandAndStatus(brand, status.name())).thenReturn(entities);
        } else if (brand != null) {
            when(repository.findByBrand(brand)).thenReturn(entities);
        } else if (status != null) {
            when(repository.findByStatus(status.name())).thenReturn(entities);
        }

        if (entities != null && entities.isPresent() && !entities.get().isEmpty()) {
            DeviceEntity entity = entities.get().getFirst();
            Device device = new Device(entity.id(), entity.name(), entity.brand(), DeviceStatusEnum.valueOf(entity.status()), LOCAL_CREATED_TIME, LOCAL_UPDATED_TIME);
            when(mapper.toDomain(entity)).thenReturn(device);
        }

        ResponseEntity<?> response = deviceService.getDevicesByBrandOrStatus(brand, status);

        assertEquals(expectedStatus, response.getStatusCode());
    }

    static Stream<Arguments> getDevicesByBrandOrStatusTestCases() {
        DeviceEntity entity = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DEVICE_STATUS.name(), CREATED_TIME, UPDATED_TIME);
        List<DeviceEntity> entities = List.of(entity);

        return Stream.of(
                Arguments.of(DEVICE_BRAND, DEVICE_STATUS, Optional.of(entities), HttpStatus.OK),
                Arguments.of(DEVICE_BRAND, null, Optional.of(entities), HttpStatus.OK),
                Arguments.of(null, DEVICE_STATUS, Optional.of(entities), HttpStatus.OK),
                Arguments.of(DEVICE_BRAND, DEVICE_STATUS, Optional.of(Collections.emptyList()), HttpStatus.NO_CONTENT),
                Arguments.of(null, null, null, HttpStatus.BAD_REQUEST)
        );
    }

    @ParameterizedTest
    @MethodSource("updateDeviceTestCases")
    @DisplayName("Should update device with different scenarios")
    void shouldUpdateDevice(UpdateDeviceRequest request, Optional<DeviceEntity> existingEntity, HttpStatus expectedStatus) {
        when(repository.findById(request.id())).thenReturn(existingEntity);

        if (existingEntity.isPresent() && !existingEntity.get().status().equals(DeviceStatusEnum.IN_USE.getDescription())) {
            DeviceEntity updatedEntity = new DeviceEntity(
                    request.id(),
                    request.name() != null ? request.name() : existingEntity.get().name(),
                    request.brand() != null ? request.brand() : existingEntity.get().brand(),
                    request.status() != null ? request.status().getDescription() : existingEntity.get().status(),
                    existingEntity.get().createdTime(),
                    UPDATED_TIME
            );

            Device device = new Device(updatedEntity.id(), updatedEntity.name(), updatedEntity.brand(),
                    DeviceStatusEnum.valueOf(updatedEntity.status()), LOCAL_CREATED_TIME, LOCAL_UPDATED_TIME);

            when(repository.update(any(DeviceEntity.class))).thenReturn(updatedEntity);
            when(mapper.toDomain(updatedEntity)).thenReturn(device);
        }

        ResponseEntity<?> response = deviceService.updateDevice(request);

        assertEquals(expectedStatus, response.getStatusCode());
    }

    static Stream<Arguments> updateDeviceTestCases() {
        UpdateDeviceRequest validRequest = new UpdateDeviceRequest(DEVICE_ID, "Updated iPhone", "Apple", DeviceStatusEnum.INACTIVE);
        DeviceEntity availableDevice = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DeviceStatusEnum.AVAILABLE.getDescription(), CREATED_TIME, UPDATED_TIME);
        DeviceEntity inUseDevice = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DeviceStatusEnum.IN_USE.getDescription(), CREATED_TIME, UPDATED_TIME);

        return Stream.of(
                Arguments.of(validRequest, Optional.of(availableDevice), HttpStatus.OK),
                Arguments.of(validRequest, Optional.of(inUseDevice), HttpStatus.BAD_REQUEST),
                Arguments.of(validRequest, Optional.empty(), HttpStatus.NOT_FOUND)
        );
    }

    @ParameterizedTest
    @MethodSource("updateDeviceStatusTestCases")
    @DisplayName("Should update device status with different scenarios")
    void shouldUpdateDeviceStatus(String id, DeviceStatusEnum newStatus, Optional<DeviceEntity> existingEntity, HttpStatus expectedStatus) {
        when(repository.findById(id)).thenReturn(existingEntity);

        if (existingEntity.isPresent()) {
            DeviceEntity updatedEntity = new DeviceEntity(
                    id,
                    existingEntity.get().name(),
                    existingEntity.get().brand(),
                    newStatus.getDescription(),
                    existingEntity.get().createdTime(),
                    UPDATED_TIME
            );

            Device device = new Device(updatedEntity.id(), updatedEntity.name(), updatedEntity.brand(),
                    newStatus, LOCAL_CREATED_TIME, LOCAL_UPDATED_TIME);

            when(repository.update(any(DeviceEntity.class))).thenReturn(updatedEntity);
            when(mapper.toDomain(updatedEntity)).thenReturn(device);
        }

        ResponseEntity<?> response = deviceService.updateDeviceStatus(id, newStatus);

        assertEquals(expectedStatus, response.getStatusCode());
    }

    static Stream<Arguments> updateDeviceStatusTestCases() {
        DeviceEntity availableDevice = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DeviceStatusEnum.AVAILABLE.getDescription(), CREATED_TIME, UPDATED_TIME);

        return Stream.of(
                Arguments.of(DEVICE_ID, DeviceStatusEnum.INACTIVE, Optional.of(availableDevice), HttpStatus.OK),
                Arguments.of(DEVICE_ID, DeviceStatusEnum.IN_USE, Optional.of(availableDevice), HttpStatus.OK),
                Arguments.of("non-existent-id", DeviceStatusEnum.AVAILABLE, Optional.empty(), HttpStatus.NOT_FOUND)
        );
    }

    @ParameterizedTest
    @MethodSource("deleteDeviceTestCases")
    @DisplayName("Should delete device with different scenarios")
    void shouldDeleteDevice(String id, Optional<DeviceEntity> existingEntity, HttpStatus expectedStatus) {
        when(repository.findById(id)).thenReturn(existingEntity);

        ResponseEntity<?> response = deviceService.deleteDevice(id);

        assertEquals(expectedStatus, response.getStatusCode());

        if (existingEntity.isPresent() && !existingEntity.get().status().equals(DeviceStatusEnum.IN_USE.getDescription())) {
            verify(repository).deleteById(id);
        }
    }

    static Stream<Arguments> deleteDeviceTestCases() {
        DeviceEntity availableDevice = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DeviceStatusEnum.AVAILABLE.getDescription(), CREATED_TIME, UPDATED_TIME);
        DeviceEntity inUseDevice = new DeviceEntity(DEVICE_ID, DEVICE_NAME, DEVICE_BRAND, DeviceStatusEnum.IN_USE.getDescription(), CREATED_TIME, UPDATED_TIME);

        return Stream.of(
                Arguments.of(DEVICE_ID, Optional.of(availableDevice), HttpStatus.OK),
                Arguments.of(DEVICE_ID, Optional.of(inUseDevice), HttpStatus.BAD_REQUEST),
                Arguments.of("non-existent-id", Optional.empty(), HttpStatus.NOT_FOUND)
        );
    }
}