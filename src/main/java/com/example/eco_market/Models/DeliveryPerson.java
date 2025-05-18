package com.example.eco_market.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "delivery_persons")
public class DeliveryPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id")
    private DeliveryZone zone;

    @Column(name = "is_available")
    private Boolean isAvailable = true;
} 