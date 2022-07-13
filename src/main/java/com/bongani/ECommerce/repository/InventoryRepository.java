package com.bongani.ECommerce.repository;

import com.bongani.ECommerce.model.Inventory;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
}
