package com.example.eco_market.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "delivery_zones")
public class DeliveryZone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "center_latitude")
    private Double centerLatitude;

    @Column(name = "center_longitude")
    private Double centerLongitude;

    @Column(name = "radius_km")
    private Double radiusKm; // радиус зоны доставки в километрах
} 