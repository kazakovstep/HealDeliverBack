package com.example.eco_market.Services;

import com.example.eco_market.Models.DeliveryZone;
import com.example.eco_market.Repositories.DeliveryZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class DeliveryZoneService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryZoneService.class);

    @Autowired
    private DeliveryZoneRepository deliveryZoneRepository;

    @PostConstruct
    public void initMockData() {
        // Проверяем, есть ли уже зоны в базе
        if (deliveryZoneRepository.count() == 0) {
            // Создаем моковые зоны доставки
            List<DeliveryZone> mockZones = List.of(
                createZone("Центральный район", 55.7558, 37.6173, 7.0),
                createZone("Северный район", 55.8352, 37.5115, 6.0),
                createZone("Южный район", 55.6558, 37.6173, 6.5),
                createZone("Западный район", 55.7558, 37.4173, 6.0),
                createZone("Восточный район", 55.7558, 37.8173, 6.5),
                createZone("Юго-Западный район", 55.6816, 37.5153, 6.0),
                createZone("Северо-Западный район", 55.8352, 37.4173, 6.0),
                createZone("Северо-Восточный район", 55.8352, 37.8173, 6.0),
                createZone("Юго-Восточный район", 55.6558, 37.8173, 6.0)
            );

            deliveryZoneRepository.saveAll(mockZones);
            logger.info("Инициализированы зоны доставки: {}", mockZones.size());
        }
    }

    private DeliveryZone createZone(String name, Double lat, Double lon, Double radius) {
        DeliveryZone zone = new DeliveryZone();
        zone.setName(name);
        zone.setCenterLatitude(lat);
        zone.setCenterLongitude(lon);
        zone.setRadiusKm(radius);
        return zone;
    }

    public DeliveryZone findZoneForCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new RuntimeException("Координаты доставки не указаны");
        }

        List<DeliveryZone> allZones = deliveryZoneRepository.findAll();
        logger.info("Поиск зоны для координат: lat={}, lon={}", latitude, longitude);
        
        for (DeliveryZone zone : allZones) {
            double distance = calculateDistance(latitude, longitude, zone.getCenterLatitude(), zone.getCenterLongitude());
            logger.info("Зона '{}': расстояние {} км, радиус {} км", zone.getName(), distance, zone.getRadiusKm());
            if (distance <= zone.getRadiusKm()) {
                logger.info("Найдена подходящая зона: {}", zone.getName());
                return zone;
            }
        }
        
        throw new RuntimeException("Не найдена подходящая зона доставки");
    }

    private boolean isPointInZone(Double pointLat, Double pointLon, DeliveryZone zone) {
        double distance = calculateDistance(
            pointLat, pointLon,
            zone.getCenterLatitude(), zone.getCenterLongitude()
        );
        return distance <= zone.getRadiusKm();
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            throw new RuntimeException("Некорректные координаты");
        }

        final double R = 6371.0; // Радиус Земли в километрах
        
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                  Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                  Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
} 