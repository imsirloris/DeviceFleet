package com.loris.devicefleet.adapters.rest;

import com.loris.devicefleet.application.dto.response.ApiResponse;
import com.loris.devicefleet.application.service.DeviceService;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return deviceService.createDevice(device);
    }

    @GetMapping("/fetchAll")
    ResponseEntity<?> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/fetchById")
    ResponseEntity<?> getDeviceById(@RequestParam String id) {
        return deviceService.getDeviceById(id);
    }

    @GetMapping("/fetchBy")
    ResponseEntity<?> getDevicesByBrandOrStatus(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceStatusEnum status) {
        return deviceService.getDevicesByBrandOrStatus(brand, status.getDescription());
    }

    @PutMapping("/update")
    ResponseEntity<?> updateDevice(@RequestBody Device device) {
        log.info("Updating device: {}", device);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/delete")
    ResponseEntity<Device> deleteDevice(@RequestParam Long id) {
        log.info("Deleting device with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
