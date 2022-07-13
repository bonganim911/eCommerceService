package com.bongani.ECommerce.repository;

import com.bongani.ECommerce.model.QuantityDiscount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuantityDiscountRepository extends CrudRepository<QuantityDiscount, Long> {
    Optional<QuantityDiscount> findByInventoryId(Integer inventoryId);
}
