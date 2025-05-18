package com.example.eco_market.Services;

import com.example.eco_market.DTO.DeliveryPersonDTO;
import com.example.eco_market.Models.DeliveryPerson;
import com.example.eco_market.Models.DeliveryZone;
import com.example.eco_market.Repositories.DeliveryPersonRepository;
import com.example.eco_market.Repositories.DeliveryZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DeliveryPersonService {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @Autowired
    private DeliveryZoneRepository deliveryZoneRepository;

    public DeliveryPerson createDeliveryPerson(DeliveryPerson person) {
        return deliveryPersonRepository.save(person);
    }

    public void updateDeliveryPerson(DeliveryPersonDTO updatedDeliveryPerson) {
        DeliveryPerson person = deliveryPersonRepository.findById(updatedDeliveryPerson.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + updatedDeliveryPerson.getId()));

        if(updatedDeliveryPerson.getName()!= null){
            person.setName(updatedDeliveryPerson.getName());
        }

				if(updatedDeliveryPerson.getPhone()!= null){
						person.setPhone(updatedDeliveryPerson.getPhone());
				}

				if(updatedDeliveryPerson.getZone()!= null){
						person.setZone(updatedDeliveryPerson.getZone());
				}
        
        deliveryPersonRepository.save(person);
    }

		public boolean deleteDeliveryPerson(Long personId) {
				if (deliveryPersonRepository.findById(personId).isPresent()) {
						deliveryPersonRepository.deleteById(personId);
						return true;
				}
				return false;
		}


    @PostConstruct
    public void initMockData() {
        // Проверяем, есть ли уже доставщики в базе
        if (deliveryPersonRepository.count() == 0) {
            // Получаем все зоны
            List<DeliveryZone> zones = deliveryZoneRepository.findAll();

            // Создаем моковых доставщиков для каждой зоны
            for (DeliveryZone zone : zones) {
                // Создаем 2-3 доставщика для каждой зоны
                int couriersCount = new Random().nextInt(2) + 2;
                for (int i = 0; i < couriersCount; i++) {
                    DeliveryPerson courier = new DeliveryPerson();
                    courier.setName(getRandomName());
                    courier.setPhone(getRandomPhone());
                    courier.setZone(zone);
                    courier.setIsAvailable(true);
                    deliveryPersonRepository.save(courier);
                }
            }
        }
    }

    private String getRandomName() {
        String[] firstNames = {"Иван", "Алексей", "Дмитрий", "Сергей", "Андрей", "Максим", "Александр"};
        String[] lastNames = {"Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов", "Попов", "Васильев"};
        
        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " + 
               lastNames[random.nextInt(lastNames.length)];
    }

    private String getRandomPhone() {
        Random random = new Random();
        return "+7" + String.format("%010d", random.nextInt(1000000000));
    }

    public DeliveryPerson assignDeliveryPerson(Long zoneId) {
        List<DeliveryPerson> availableCouriers = deliveryPersonRepository.findByZoneIdAndIsAvailableTrue(zoneId);
        if (availableCouriers.isEmpty()) {
            throw new RuntimeException("Нет доступных доставщиков в данной зоне");
        }
        
        // Выбираем случайного доставщика из доступных
        Random random = new Random();
        DeliveryPerson courier = availableCouriers.get(random.nextInt(availableCouriers.size()));
        courier.setIsAvailable(false);
        return deliveryPersonRepository.save(courier);
    }

    public void releaseDeliveryPerson(Long deliveryPersonId) {
        deliveryPersonRepository.findById(deliveryPersonId).ifPresent(courier -> {
            courier.setIsAvailable(true);
            deliveryPersonRepository.save(courier);
        });
    }
} 