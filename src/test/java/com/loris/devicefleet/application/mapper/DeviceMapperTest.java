package com.loris.devicefleet.application.mapper;

import com.loris.devicefleet.adapters.persistence.mongo.entity.DeviceEntity;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DeviceMapperTest {

    private final DeviceMapper deviceMapper = Mappers.getMapper(DeviceMapper.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @ParameterizedTest
    @MethodSource("deviceDomainToEntitySource")
    @DisplayName("Should map domain to entity correctly")
    void shouldMapDomainToEntity(Device device, DeviceEntity expectedEntity) {
        DeviceEntity result = deviceMapper.toEntity(device);

        assertThat(result.name()).isEqualTo(expectedEntity.name());
        assertThat(result.brand()).isEqualTo(expectedEntity.brand());
        assertThat(result.status()).isEqualTo(expectedEntity.status());
    }

    @ParameterizedTest
    @MethodSource("deviceEntityToDomainSource")
    @DisplayName("Should map entity to domain correctly")
    void shouldMapEntityToDomain(DeviceEntity entity, Device expectedDevice) {
        Device result = deviceMapper.toDomain(entity);

        assertThat(result.id()).isEqualTo(expectedDevice.id());
        assertThat(result.name()).isEqualTo(expectedDevice.name());
        assertThat(result.brand()).isEqualTo(expectedDevice.brand());
        assertThat(result.status()).isEqualTo(expectedDevice.status());
        assertThat(result.createdTime()).isEqualTo(expectedDevice.createdTime());
        assertThat(result.updatedTime()).isEqualTo(expectedDevice.updatedTime());
    }

    @ParameterizedTest
    @MethodSource("instantToLocalDateTimeSource")
    @DisplayName("Should convert Instant to LocalDateTime correctly")
    void shouldConvertInstantToLocalDateTime(Instant instant, LocalDateTime expected) {
        LocalDateTime result = deviceMapper.instantToLocalDateTime(instant);
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> deviceDomainToEntitySource() {
        return Stream.of(
                Arguments.of(
                        new Device("683c817f1d1ca53dfe24c1df", "iPhone 14", "Apple", DeviceStatusEnum.AVAILABLE,
                                parseLocalDateTime("2025-06-01T16:36:15.255"),
                                parseLocalDateTime("2025-06-01T16:36:15.255")),
                        new DeviceEntity(null, "iPhone 14", "Apple", "AVAILABLE", null, null)
                ),
                Arguments.of(
                        new Device("123", "Galaxy S23", "Samsung", DeviceStatusEnum.IN_USE, null, null),
                        new DeviceEntity(null, "Galaxy S23", "Samsung", "IN-USE", null, null)
                ),
                Arguments.of(
                        new Device("456", "Pixel 7", "Google", DeviceStatusEnum.INACTIVE,
                                parseLocalDateTime("2025-05-01T10:30:45.123"),
                                parseLocalDateTime("2025-05-02T09:15:30.456")),
                        new DeviceEntity(null, "Pixel 7", "Google", "INACTIVE", null, null)
                )
        );
    }

    private static Stream<Arguments> deviceEntityToDomainSource() {
        return Stream.of(
                Arguments.of(
                        new DeviceEntity("683c817f1d1ca53dfe24c1df", "iPhone 14", "Apple", "AVAILABLE",
                                parseInstant("2025-06-01T16:36:15.255"),
                                parseInstant("2025-06-01T16:36:15.255")),
                        new Device("683c817f1d1ca53dfe24c1df", "iPhone 14", "Apple", DeviceStatusEnum.AVAILABLE,
                                parseLocalDateTime("2025-06-01T16:36:15.255"),
                                parseLocalDateTime("2025-06-01T16:36:15.255"))
                ),
                Arguments.of(
                        new DeviceEntity("123", "Galaxy S23", "Samsung", "IN-USE", null, null),
                        new Device("123", "Galaxy S23", "Samsung", DeviceStatusEnum.IN_USE, null, null)
                ),
                Arguments.of(
                        new DeviceEntity("456", "Pixel 7", "Google", "INACTIVE",
                                parseInstant("2025-05-01T10:30:45.123"),
                                parseInstant("2025-05-02T09:15:30.456")),
                        new Device("456", "Pixel 7", "Google", DeviceStatusEnum.INACTIVE,
                                parseLocalDateTime("2025-05-01T10:30:45.123"),
                                parseLocalDateTime("2025-05-02T09:15:30.456"))
                )
        );
    }

    private static Stream<Arguments> instantToLocalDateTimeSource() {
        return Stream.of(
                Arguments.of(parseInstant("2025-06-01T16:36:15.255"), parseLocalDateTime("2025-06-01T16:36:15.255")),
                Arguments.of(null, null),
                Arguments.of(Instant.EPOCH, LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault()))
        );
    }

    private static LocalDateTime parseLocalDateTime(String dateTime) {
        return dateTime != null ? LocalDateTime.parse(dateTime, formatter) : null;
    }

    private static Instant parseInstant(String dateTime) {
        return dateTime != null ? LocalDateTime.parse(dateTime, formatter)
                .atZone(ZoneId.systemDefault()).toInstant() : null;
    }
}