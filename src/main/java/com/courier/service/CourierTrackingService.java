package com.courier.service;

import com.courier.model.CourierLocation;
import com.courier.model.Store;
import com.courier.repository.CourierLocationRepository;
import com.courier.repository.StoreRepository;
import com.courier.util.DistanceCalculator;
import com.courier.util.HaversineDistanceCalculator;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierTrackingService {
    private static final Logger logger = LoggerFactory.getLogger(CourierTrackingService.class);
    
    private final CourierLocationRepository courierLocationRepository;
    private final StoreRepository storeRepository;
    private final Cache<String, Boolean> storeEntryCache;
    private final DistanceCalculator distanceCalculator = new HaversineDistanceCalculator();
    
    private static final double STORE_RADIUS = 100; // metre

    public void trackLocation(String courierId, double lat, double lng) {
        MDC.put("courierId", courierId);
        try {
            logger.info("Kurye konumu güncelleniyor: lat={}, lng={}", lat, lng);
            
            CourierLocation location = new CourierLocation();
            location.setCourierId(courierId);
            location.setLat(lat);
            location.setLng(lng);
            location.setTimestamp(LocalDateTime.now());
            
            courierLocationRepository.save(location);
            checkStoreProximity(location);
            
            logger.info("Kurye konumu başarıyla güncellendi");
        } finally {
            MDC.remove("courierId");
        }
    }

    private void checkStoreProximity(CourierLocation location) {
        List<Store> stores = storeRepository.findAll();
        
        for (Store store : stores) {
            double distance = distanceCalculator.calculateDistance(
                location.getLat(), location.getLng(),
                store.getLat(), store.getLng()
            );
            
            MDC.put("storeId", store.getId().toString());
            MDC.put("distance", String.valueOf(distance));
            
            try {
                if (distance <= STORE_RADIUS) {
                    String cacheKey = location.getCourierId() + "_" + store.getId();
                    
                    if (storeEntryCache.getIfPresent(cacheKey) == null) {
                        storeEntryCache.put(cacheKey, true);
                        logger.info("Kurye mağaza yakınında: storeName={}, distance={}m", store.getName(), distance);
                    } else {
                        logger.debug("Kurye zaten mağaza yakınında: storeName={}, distance={}m", store.getName(), distance);
                    }
                }
            } finally {
                MDC.remove("storeId");
                MDC.remove("distance");
            }
        }
    }

    public double getTotalTravelDistance(String courierId) {
        MDC.put("courierId", courierId);
        try {
            logger.info("Kurye toplam mesafesi hesaplanıyor");
            
            List<CourierLocation> locations = courierLocationRepository.findByCourierIdOrderByTimestampAsc(courierId);
            double totalDistance = 0;
            
            for (int i = 1; i < locations.size(); i++) {
                CourierLocation prev = locations.get(i - 1);
                CourierLocation curr = locations.get(i);
                
                totalDistance += distanceCalculator.calculateDistance(
                    prev.getLat(), prev.getLng(),
                    curr.getLat(), curr.getLng()
                );
            }
            
            logger.info("Kurye toplam mesafesi hesaplandı: {}m", totalDistance);
            return totalDistance;
        } finally {
            MDC.remove("courierId");
        }
    }
} 