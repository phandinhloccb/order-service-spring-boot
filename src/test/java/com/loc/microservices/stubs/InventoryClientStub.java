package com.loc.microservices.stubs;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InventoryClientStub {
    /**
     * Configures MockMvc stub for inventory service
     * @param mockMvc the MockMvc instance
     * @param skuCode the SKU code to check
     * @param quantity the quantity to check
     * @return true if product is in stock, false otherwise
     */
    public static boolean stubInventoryCall(MockMvc mockMvc, String skuCode, Integer quantity) throws Exception {
        boolean inStock = quantity <= 100;
        
        mockMvc.perform(get("/mock/api/inventory")
                .param("skuCode", skuCode)
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(String.valueOf(inStock)));
        
        return inStock;
    }
}
