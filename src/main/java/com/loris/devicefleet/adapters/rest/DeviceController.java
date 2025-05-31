package com.loris.devicefleet.adapters.rest;

import com.loris.devicefleet.domain.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
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

    @PostMapping
    ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO deviceDTO) {

        return ResponseEntity.ok(deviceDTO);
    }




}
