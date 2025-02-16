package com.dinh.microservices.inventoryservice;

import com.dinh.microservices.inventoryservice.model.Inventory;
import com.dinh.microservices.inventoryservice.repository.IInventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(IInventoryRepository inventoryRepository) {
		return (args) -> {
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("Iphone_13");
			inventory1.setQuantity(100);

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("Iphone_13_red");
			inventory2.setQuantity(0);

			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
		};
	}
}
