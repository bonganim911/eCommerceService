package com.bongani.ECommerce.service;

import com.bongani.ECommerce.dtos.TotalCostResponse;

import java.util.List;

public interface ECommerceService {
    TotalCostResponse getPrice(List<String> inventoryLists);
}
