package com.loris.devicefleet.adapters.rest;

import com.loris.devicefleet.application.dto.CreateDeviceRequest;
import com.loris.devicefleet.application.dto.UpdateDeviceRequest;
import com.loris.devicefleet.application.service.DeviceService;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceControllerTest {

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private DeviceController deviceController;

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2025, 6, 1, 16, 36, 15, 255000000);


    private static final String BASE_ID = "683c817f1d1ca53dfe24c1df";
    private static final String BASE_NAME = "iPhone 14";
    private static final String BASE_BRAND = "Apple";
    private static final DeviceStatusEnum BASE_STATUS = DeviceStatusEnum.AVAILABLE;

    private static Stream<Arguments> provideDevicesForCreate() {
        return Stream.of(
                Arguments.of(
                        new CreateDeviceRequest(BASE_NAME, BASE_BRAND, BASE_STATUS),
                        new Device(BASE_ID, BASE_NAME, BASE_BRAND, BASE_STATUS, FIXED_DATE, FIXED_DATE)
                ),
                Arguments.of(
                        new CreateDeviceRequest("Galaxy S23", "Samsung", DeviceStatusEnum.AVAILABLE),
                        new Device("783c817f1d1ca53dfe24c1dg", "Galaxy S23", "Samsung", BASE_STATUS, FIXED_DATE, FIXED_DATE)
                )
        );
    }

    private static Stream<Arguments> provideDevicesForFetchById() {
        return Stream.of(
                Arguments.of(
                        BASE_ID,
                        new Device(BASE_ID, BASE_NAME, BASE_BRAND, BASE_STATUS, FIXED_DATE, FIXED_DATE)
                ),
                Arguments.of(
                        "783c817f1d1ca53dfe24c1dg",
                        new Device("783c817f1d1ca53dfe24c1dg", "Galaxy S23", "Samsung", BASE_STATUS, FIXED_DATE, FIXED_DATE)
                )
        );
    }

    private static Stream<Arguments> provideDevicesForFetchByBrandOrStatus() {
        Device appleDevice = new Device(BASE_ID, BASE_NAME, BASE_BRAND, BASE_STATUS, FIXED_DATE, FIXED_DATE);
        Device samsungDevice = new Device("783c817f1d1ca53dfe24c1dg", "Galaxy S23", "Samsung", BASE_STATUS, FIXED_DATE, FIXED_DATE);

        return Stream.of(
                Arguments.of(BASE_BRAND, null, List.of(appleDevice)),
                Arguments.of(null, BASE_STATUS, List.of(appleDevice, samsungDevice)),
                Arguments.of(BASE_BRAND, BASE_STATUS, List.of(appleDevice))
        );
    }

    private static Stream<Arguments> provideDevicesForUpdate() {
        return Stream.of(
                Arguments.of(
                        new UpdateDeviceRequest(BASE_ID, "iPhone 14 Pro", BASE_BRAND, BASE_STATUS),
                        new Device(BASE_ID, "iPhone 14 Pro", BASE_BRAND, BASE_STATUS, FIXED_DATE, FIXED_DATE)
                ),
                Arguments.of(
                        new UpdateDeviceRequest("783c817f1d1ca53dfe24c1dg", "Galaxy S23 Ultra", "Samsung", BASE_STATUS),
                        new Device("783c817f1d1ca53dfe24c1dg", "Galaxy S23 Ultra", "Samsung", BASE_STATUS, FIXED_DATE, FIXED_DATE)
                )
        );
    }

    private static Stream<Arguments> provideDevicesForUpdateStatus() {
        return Stream.of(
                Arguments.of(
                        BASE_ID, DeviceStatusEnum.INACTIVE,
                        new Device(BASE_ID, BASE_NAME, BASE_BRAND, DeviceStatusEnum.INACTIVE, FIXED_DATE, FIXED_DATE)
                ),
                Arguments.of(
                        "783c817f1d1ca53dfe24c1dg", DeviceStatusEnum.INACTIVE,
                        new Device("783c817f1d1ca53dfe24c1dg", "Galaxy S23", "Samsung", DeviceStatusEnum.INACTIVE, FIXED_DATE, FIXED_DATE)
                )
        );
    }

    private static Stream<Arguments> provideDevicesForDelete() {
        return Stream.of(
                Arguments.of(BASE_ID, "Device successfully deleted"),
                Arguments.of("783c817f1d1ca53dfe24c1dg", "Device successfully deleted")
        );
    }

    private static Stream<Arguments> provideDevicesForGetAll() {
        Device appleDevice = new Device(BASE_ID, BASE_NAME, BASE_BRAND, BASE_STATUS, FIXED_DATE, FIXED_DATE);
        Device samsungDevice = new Device("783c817f1d1ca53dfe24c1dg", "Galaxy S23", "Samsung", BASE_STATUS, FIXED_DATE, FIXED_DATE);

        return Stream.of(
                Arguments.of(List.of(appleDevice)),
                Arguments.of(List.of(appleDevice, samsungDevice)),
                Arguments.of(List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("provideDevicesForCreate")
    @DisplayName("Test createDevice with various device data")
    void createDevice_ReturnsCreatedDevice(CreateDeviceRequest request, Device expectedDevice) {
        ResponseEntity<Device> responseEntity = ResponseEntity.ok(expectedDevice);
        when(deviceService.createDevice(any())).thenReturn(responseEntity);

        ResponseEntity<Device> response = deviceController.createDevice(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDevice, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("provideDevicesForGetAll")
    @DisplayName("Test getAllDevices with various device lists")
    void getAllDevices_ReturnsDeviceList(List<Device> expectedDevices) {
        ResponseEntity<List<Device>> responseEntity = ResponseEntity.ok(expectedDevices);
        doReturn(responseEntity).when(deviceService).getAllDevices();

        ResponseEntity<?> response = deviceController.getAllDevices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDevices, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("provideDevicesForFetchById")
    @DisplayName("Test getDeviceById with various device IDs")
    void getDeviceById_ReturnsDevice(String id, Device expectedDevice) {
        ResponseEntity<Device> responseEntity = ResponseEntity.ok(expectedDevice);
        doReturn(responseEntity).when(deviceService).getDeviceById(id);

        ResponseEntity<?> response = deviceController.getDeviceById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDevice, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("provideDevicesForFetchByBrandOrStatus")
    @DisplayName("Test getDevicesByBrandOrStatus with various combinations")
    void getDevicesByBrandOrStatus_ReturnsDevices(String brand, DeviceStatusEnum status, List<Device> expectedDevices) {
        ResponseEntity<List<Device>> responseEntity = ResponseEntity.ok(expectedDevices);
        doReturn(responseEntity).when(deviceService).getDevicesByBrandOrStatus(brand, status);

        ResponseEntity<?> response = deviceController.getDevicesByBrandOrStatus(brand, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDevices, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("provideDevicesForUpdate")
    @DisplayName("Test updateDevice with various update requests")
    void updateDevice_ReturnsUpdatedDevice(UpdateDeviceRequest request, Device expectedDevice) {
        ResponseEntity<Device> responseEntity = ResponseEntity.ok(expectedDevice);
        doReturn(responseEntity).when(deviceService).updateDevice(request);

        ResponseEntity<?> response = deviceController.updateDevice(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDevice, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("provideDevicesForUpdateStatus")
    @DisplayName("Test updateDeviceStatus with various status updates")
    void updateDeviceStatus_ReturnsUpdatedDevice(String id, DeviceStatusEnum status, Device expectedDevice) {
        ResponseEntity<Device> responseEntity = ResponseEntity.ok(expectedDevice);
        doReturn(responseEntity).when(deviceService).updateDeviceStatus(id, status);

        ResponseEntity<?> response = deviceController.updateDeviceStatus(id, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDevice, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("provideDevicesForDelete")
    @DisplayName("Test deleteDevice with various device IDs")
    void deleteDevice_ReturnsSuccessMessage(String id, String expectedMessage) {
        ResponseEntity<String> responseEntity = ResponseEntity.ok(expectedMessage);
        doReturn(responseEntity).when(deviceService).deleteDevice(id);

        ResponseEntity<?> response = deviceController.deleteDevice(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }
}