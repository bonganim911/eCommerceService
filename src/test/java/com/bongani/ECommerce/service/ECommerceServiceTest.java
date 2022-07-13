package com.bongani.ECommerce.service;

import com.bongani.ECommerce.dtos.TotalCostResponse;
import com.bongani.ECommerce.model.Inventory;
import com.bongani.ECommerce.model.QuantityDiscount;
import com.bongani.ECommerce.repository.InventoryRepository;
import com.bongani.ECommerce.repository.QuantityDiscountRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ECommerceServiceTest {

    @InjectMocks
    private ECommerceServiceImpl eCommerceService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private QuantityDiscountRepository quantityDiscountRepository;

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

    @Test
    void shouldReturnPriceGivenInventoryIdWithDiscount() {
        Inventory inventory = new Inventory();
        List<String> inventoryLists = new ArrayList<>();
        inventory.setPrice(BigDecimal.valueOf(100));
        inventoryLists.add("001");
        inventoryLists.add("001");
        inventoryLists.add("001");

        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setQuantity(3);
        quantityDiscount.setPrice(BigDecimal.valueOf(200));

        BigDecimal expectedTotalCosts = BigDecimal.valueOf(200);

        Optional<Inventory> databaseResponse = Optional.of(inventory);
        Optional<QuantityDiscount> quantityDiscountResponse = Optional.of(quantityDiscount);

        when(inventoryRepository.findById(any())).thenReturn(databaseResponse);
        when(quantityDiscountRepository.findByInventoryId(any())).thenReturn(quantityDiscountResponse);

        TotalCostResponse price = eCommerceService.getPrice(inventoryLists);

        assertEquals(expectedTotalCosts, price.getPrice());

    }

    @Test
    void shouldReturnPriceGivenInventoryIdWithDiscountWhenExtraQuantity() {
        Inventory inventory = new Inventory();
        List<String> inventoryLists = new ArrayList<>();
        inventory.setPrice(BigDecimal.valueOf(100));
        inventoryLists.add("001");
        inventoryLists.add("001");
        inventoryLists.add("001");
        inventoryLists.add("001");

        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setQuantity(3);
        quantityDiscount.setPrice(BigDecimal.valueOf(200));

        BigDecimal expectedTotalCosts = BigDecimal.valueOf(300);

        Optional<Inventory> databaseResponse = Optional.of(inventory);
        Optional<QuantityDiscount> quantityDiscountResponse = Optional.of(quantityDiscount);

        when(inventoryRepository.findById(any())).thenReturn(databaseResponse);
        when(quantityDiscountRepository.findByInventoryId(any())).thenReturn(quantityDiscountResponse);

        TotalCostResponse price = eCommerceService.getPrice(inventoryLists);

        assertEquals(expectedTotalCosts, price.getPrice());

    }

    @Test
    void shouldReturnPriceGivenInventoryIdWithDiscountWhenExtraQuantityWithMultipleInventories() {
        Inventory inventory = new Inventory();
        List<String> inventoryLists = new ArrayList<>();
        inventory.setPrice(BigDecimal.valueOf(100));

        Inventory secondInventory = new Inventory();
        secondInventory.setPrice(BigDecimal.valueOf(80));

        Inventory thirdInventory = new Inventory();
        thirdInventory.setPrice(BigDecimal.valueOf(50));

        inventoryLists.add("001");
        inventoryLists.add("002");
        inventoryLists.add("003");
        inventoryLists.add("001");
        inventoryLists.add("001");
        inventoryLists.add("002");
        inventoryLists.add("002");
        inventoryLists.add("001");

        QuantityDiscount quantityDiscountForFirstInventory = new QuantityDiscount();
        quantityDiscountForFirstInventory.setQuantity(3);
        quantityDiscountForFirstInventory.setPrice(BigDecimal.valueOf(200));

        QuantityDiscount quantityDiscountForSecondInventory = new QuantityDiscount();
        quantityDiscountForSecondInventory.setQuantity(2);
        quantityDiscountForSecondInventory.setPrice(BigDecimal.valueOf(120));

        BigDecimal expectedTotalCosts = BigDecimal.valueOf(550);

        Optional<Inventory> databaseResponse = Optional.of(inventory);
        Optional<Inventory> secondDatabaseResponse = Optional.of(secondInventory);
        Optional<Inventory> thirdDatabaseResponse = Optional.of(thirdInventory);

        Optional<QuantityDiscount> quantityDiscountResponseForFirstInventory = Optional.of(quantityDiscountForFirstInventory);
        Optional<QuantityDiscount> quantityDiscountResponseForSecondInventory = Optional.of(quantityDiscountForFirstInventory);
        Optional<QuantityDiscount> quantityDiscountResponseForThirdInventory = Optional.empty();

        when(inventoryRepository.findById(eq(Long.valueOf("001")))).thenReturn(databaseResponse);
        when(inventoryRepository.findById(eq(Long.valueOf("002")))).thenReturn(secondDatabaseResponse);
        when(inventoryRepository.findById(eq(Long.valueOf("003")))).thenReturn(thirdDatabaseResponse);

        when(quantityDiscountRepository.findByInventoryId(eq(Integer.parseInt("001")))).thenReturn(quantityDiscountResponseForFirstInventory);
        when(quantityDiscountRepository.findByInventoryId(eq(Integer.parseInt("002")))).thenReturn(quantityDiscountResponseForSecondInventory);
        when(quantityDiscountRepository.findByInventoryId(eq(Integer.parseInt("003")))).thenReturn(quantityDiscountResponseForThirdInventory);

        TotalCostResponse price = eCommerceService.getPrice(inventoryLists);

        assertEquals(expectedTotalCosts, price.getPrice());

    }


}
