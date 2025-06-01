package com.loris.devicefleet.adapters.rest;

import com.loris.devicefleet.application.dto.DeviceRequest;
import com.loris.devicefleet.application.service.DeviceService;
import com.loris.devicefleet.domain.model.Device;
import com.loris.devicefleet.domain.model.enums.DeviceStatusEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return deviceService.getDevicesByBrandOrStatus(brand, status);
    }

    @PutMapping("/update")
    ResponseEntity<?> updateDevice(@RequestBody DeviceRequest device) {
        return deviceService.updateDevice(device);
    }

    @PatchMapping("/updateStatus")
    ResponseEntity<?> updateDeviceStatus(@RequestParam String id, @RequestParam DeviceStatusEnum status) {
        return deviceService.updateDeviceStatus(id, status);
    }

    @DeleteMapping("/delete")
    ResponseEntity<?> deleteDevice(@RequestParam String id) {
        log.info("Deleting device with ID: {}", id);
        return deviceService.deleteDevice(id);
    }
}
