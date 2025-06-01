package com.loris.devicefleet.adapters.rest;

import com.loris.devicefleet.application.service.DeviceService;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /*
    * APIS:
    *   CREATE:
    *   FETCH:
    *       ALL
    *       SINGLE
    *       by brand or by status
    *   UPDATE:
    *       fully
    *       partially
    *   DELETE:
    */

    @PostMapping("/create")
    ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.createDevice(device));
    }

    @GetMapping("/fetchAll")
    ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = List.of(
                new Device(1L, "Device1", "BrandA", DeviceStatusEnum.AVAILABLE, LocalDateTime.now(), LocalDateTime.now()),
                new Device(2L, "Device2", "BrandB", DeviceStatusEnum.INACTIVE, LocalDateTime.now(), LocalDateTime.now())
        );
        log.info("Fetching all devices: {}", devices);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/fetchById")
    ResponseEntity<Device> getDeviceById(@RequestParam Long id) {
        Device device = new Device(id, "Device" + id, "BrandA", DeviceStatusEnum.AVAILABLE, LocalDateTime.now(), LocalDateTime.now());
        log.info("Fetching device by ID: {}", id);
        return ResponseEntity.ok(device);
    }

    @GetMapping("/fetchBy")
    ResponseEntity<List<Device>> getDevicesByBrandOrStatus(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceStatusEnum status) {

        List<Device> devices = List.of(
                new Device(1L, "Device1", brand != null ? brand : "BrandA", status != null ? status : DeviceStatusEnum.AVAILABLE, LocalDateTime.now(), LocalDateTime.now())
        );
        log.info("Fetching devices by brand or status: brand={}, status={}", brand, status);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/update")
    ResponseEntity<Device> updateDevice(@RequestBody Device device) {
        log.info("Updating device: {}", device);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteDevice(@RequestParam Long id) {
        log.info("Deleting device with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
