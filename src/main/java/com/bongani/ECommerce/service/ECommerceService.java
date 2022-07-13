package com.bongani.ECommerce.service;

import com.bongani.ECommerce.dtos.TotalCostResponse;
import com.bongani.ECommerce.model.Inventory;
import com.bongani.ECommerce.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ECommerceService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public TotalCostResponse getPrice(List<String> inventoryLists) {
        Map<String, Integer> inventoryMap = new HashMap<>();

        for(String inventory: inventoryLists){
            inventoryMap.put(inventory, inventoryMap.getOrDefault(inventory, 0) + 1);
        }

        BigDecimal totalCost = new BigDecimal(0);

        for(String inventory: inventoryMap.keySet()){
            Optional<Inventory> inventoryObject = inventoryRepository.findById(Long.valueOf(inventory));

            if (inventoryObject.isPresent()) {

                totalCost = totalCost.add(inventoryObject.get().getPrice());
            }else {
                totalCost = totalCost.add(BigDecimal.valueOf(0));
            }

        }

        return new TotalCostResponse(totalCost);
    }
}
