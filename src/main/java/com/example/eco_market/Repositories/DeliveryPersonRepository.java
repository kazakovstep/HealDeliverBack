package com.example.eco_market.Repositories;

import com.example.eco_market.Models.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    List<DeliveryPerson> findByZoneIdAndIsAvailableTrue(Long zoneId);
    List<DeliveryPerson> findByZoneId(Long zoneId);
} 