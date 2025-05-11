package com.example.eco_market.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "country")
    private String country;

    @Column(name = "amount")
    private int amount;

    @Column(name = "calories")
    private float calories;

    @Column(name = "proteins")
    private float proteins;

    @Column(name = "fats")
    private float fats;

    @Column(name = "carbohydrates")
    private float carbohydrates;

    @Column(name = "cellulose")
    private float cellulose;

    @Column(name = "category")
    private String category;

    @Column(name = "image", columnDefinition = "longtext")
    private String image;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<OrderProduct> orderProducts;
}
