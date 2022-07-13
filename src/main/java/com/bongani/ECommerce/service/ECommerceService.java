package com.bongani.ECommerce.service;

import com.bongani.ECommerce.dtos.TotalCostResponse;
import com.bongani.ECommerce.model.Inventory;
import com.bongani.ECommerce.model.QuantityDiscount;
import com.bongani.ECommerce.repository.InventoryRepository;
import com.bongani.ECommerce.repository.QuantityDiscountRepository;
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

    @Autowired
    private QuantityDiscountRepository quantityDiscountRepository;

    public TotalCostResponse getPrice(List<String> inventoryLists) {
        Map<String, Integer> inventoryMap = mapInventoryListToUniqueMapWithCount(inventoryLists);
        BigDecimal totalCost = new BigDecimal(0);

        for(String inventory: inventoryMap.keySet()){
            Optional<Inventory> inventoryObject = inventoryRepository.findById(Long.valueOf(inventory));
            Optional<QuantityDiscount> quantity = quantityDiscountRepository.findByInventoryId(Integer.parseInt(inventory));

            Integer inventoryQuantity = inventoryMap.get(inventory);

            BigDecimal inventoryCosts = getInventoryCostFromMap(inventoryObject, quantity, inventoryQuantity);

            totalCost = totalCost.add(inventoryCosts);
        }

        return new TotalCostResponse(totalCost);
    }

    private BigDecimal getInventoryCostFromMap(Optional<Inventory> inventoryObject, Optional<QuantityDiscount> quantity, Integer inventoryQuantity) {
        BigDecimal inventoryCosts = BigDecimal.ZERO;

        if(inventoryObject.isPresent()){
            BigDecimal priceForInventory = inventoryObject.get().getPrice();

            BigDecimal costOfInventory;
            if(quantity.isPresent()){
                int numberOfQuantitiesToDiscountFor = quantity.get().getQuantity();

                int multiplier = inventoryQuantity / numberOfQuantitiesToDiscountFor;

                int remainder = inventoryQuantity % numberOfQuantitiesToDiscountFor;

                costOfInventory =  quantity.get().getPrice().multiply(BigDecimal.valueOf(multiplier)).add(priceForInventory.multiply(BigDecimal.valueOf(remainder)));
            }
            else{
                costOfInventory = priceForInventory.multiply(BigDecimal.valueOf(inventoryQuantity));
            }
            inventoryCosts = inventoryCosts.add(costOfInventory);
        }
        return inventoryCosts;
    }

    private Map<String, Integer> mapInventoryListToUniqueMapWithCount(List<String> inventoryLists){
        Map<String, Integer> inventoryMap = new HashMap<>();

        for(String inventory: inventoryLists){
            inventoryMap.put(inventory, inventoryMap.getOrDefault(inventory, 0) + 1);
        }

        return inventoryMap;
    }
}
