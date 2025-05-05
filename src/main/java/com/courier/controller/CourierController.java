package com.courier.controller;

import com.courier.service.CourierTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController {
    private final CourierTrackingService courierTrackingService;

    @PostMapping("/{courierId}/location")
    public ResponseEntity<Void> trackLocation(
            @PathVariable String courierId,
            @RequestParam double lat,
            @RequestParam double lng) {
        courierTrackingService.trackLocation(courierId, lat, lng);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courierId}/distance")
    public ResponseEntity<Double> getTotalDistance(@PathVariable String courierId) {
        double distance = courierTrackingService.getTotalTravelDistance(courierId);
        return ResponseEntity.ok(distance);
    }
} 