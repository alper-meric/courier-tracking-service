package com.courier.repository;

import com.courier.model.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
    List<CourierLocation> findByCourierIdOrderByTimestampAsc(String courierId);
} 