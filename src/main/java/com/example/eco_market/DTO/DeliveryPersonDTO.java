package com.example.eco_market.DTO;

import com.example.eco_market.Models.DeliveryZone;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryPersonDTO {
		private Long id;
    private String name;
    private String phone;
    private DeliveryZone zone;
}
