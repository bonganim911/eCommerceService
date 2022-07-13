package com.bongani.ECommerce.controller;

import com.bongani.ECommerce.dtos.TotalCostResponse;
import com.bongani.ECommerce.service.ECommerceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ECommerceController.class)
public class ECommerceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ECommerceService eCommerceService;

    @Test
    public void getRolexPriceGivenArrayWithRolexID() throws Exception {
        List<String> request = new ArrayList<>();
        request.add("001");

        String requestJson = "[\"001\"]";

        TotalCostResponse checkoutTotalPriceMap = new TotalCostResponse(BigDecimal.valueOf(100));

        when(eCommerceService.getPrice(request)).thenReturn(checkoutTotalPriceMap);

        mockMvc.perform(get("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(eCommerceService, times(1)).getPrice(any());
    }
}
