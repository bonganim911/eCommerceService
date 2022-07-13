package com.bongani.ECommerce.controller;

import com.bongani.ECommerce.dtos.TotalCostResponse;
import com.bongani.ECommerce.service.ECommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ECommerceController {

    @Autowired
    private ECommerceService eCommerceService;

    @GetMapping("/checkout")
    public ResponseEntity<TotalCostResponse> getTotalPrice(@RequestBody List<String> inventories){
        return new ResponseEntity<>(eCommerceService.getPrice(inventories), HttpStatus.OK);
    }
}
