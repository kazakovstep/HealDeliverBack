package com.example.eco_market.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "cost")
    private int cost;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_latitude")
    private Double deliveryLatitude;

    @Column(name = "delivery_longitude")
    private Double deliveryLongitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_zone_id")
    private DeliveryZone deliveryZone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;
}
