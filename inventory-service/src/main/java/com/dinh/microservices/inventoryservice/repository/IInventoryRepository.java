package com.dinh.microservices.inventoryservice.repository;

import com.dinh.microservices.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, Long> {
	Optional<Inventory> findBySkuCodeIn(List<String> skuCode);
}
