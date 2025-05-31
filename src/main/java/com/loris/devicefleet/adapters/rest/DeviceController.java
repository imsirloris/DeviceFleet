package com.loris.devicefleet.adapters.rest;

import com.loris.devicefleet.domain.Device;
import com.loris.devicefleet.domain.model.Enums.DeviceStatusEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/api/devices")
public class DeviceController {

    /*
    * APIS:
    *   CREATE:
    *   FETCH:
    *       ALL
    *       SINGLE
    *       by brand
    *       by status
    *   UPDATE:
    *       fully
    *       partially
    *   DELETE:
    */

    @PostMapping("/create")
    ResponseEntity<Device> createDevice(@RequestBody Device device) {

        return ResponseEntity.ok(device);
    }

    @GetMapping("/fetchAll")
    ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = List.of(
                new Device(1L, "Device1", "BrandA", DeviceStatusEnum.AVAILABLE, LocalDateTime.now(), LocalDateTime.now()),
                new Device(2L, "Device2", "BrandB", DeviceStatusEnum.INACTIVE, LocalDateTime.now(), LocalDateTime.now())
        );
        return ResponseEntity.ok(devices);
    }



}
