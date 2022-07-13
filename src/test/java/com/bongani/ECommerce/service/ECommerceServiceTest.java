package com.bongani.ECommerce.service;

import com.bongani.ECommerce.dtos.TotalCostResponse;
import com.bongani.ECommerce.model.Inventory;
import com.bongani.ECommerce.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ECommerceServiceTest {

    @InjectMocks
    private ECommerceService eCommerceService;

    @Mock
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnPriceGivenInventoryId() {
        Inventory inventory = new Inventory();
        List<String> inventoryLists = new ArrayList<>();
        inventoryLists.add("001");

        BigDecimal expectedTotalCosts = BigDecimal.valueOf(100);
        inventory.setPrice(expectedTotalCosts);

        Optional<Inventory> databaseResponse = Optional.of(inventory);

        when(inventoryRepository.findById(any())).thenReturn(databaseResponse);

        TotalCostResponse price = eCommerceService.getPrice(inventoryLists);

        assertEquals(expectedTotalCosts, price.getPrice());

    }

    @Test
    void shouldReturnZeroPriceWhenTheInventoryDoesNotExist(){
        List<String> inventoryLists = new ArrayList<>();
        inventoryLists.add("001");

        BigDecimal expectedTotalCosts = BigDecimal.valueOf(0);

        Optional<Inventory> databaseResponse = Optional.empty();

        when(inventoryRepository.findById(any())).thenReturn(databaseResponse);

        TotalCostResponse price = eCommerceService.getPrice(inventoryLists);

        assertEquals(expectedTotalCosts, price.getPrice());
    }

}
