package com.loc.microservices.order_service_spring_boot.controller.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MockInventoryController.class)
@ActiveProfiles("dev-mock")
public class MockInventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void isInStock_withNormalProduct_shouldReturnTrue() throws Exception {
        mockMvc.perform(get("/mock/api/inventory")
                .param("skuCode", "normal-product")
                .param("quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void isInStock_withOutProduct_shouldReturnFalse() throws Exception {
        mockMvc.perform(get("/mock/api/inventory")
                .param("skuCode", "OUT-product")
                .param("quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void isInStock_withLowProductAndHighQuantity_shouldReturnFalse() throws Exception {
        mockMvc.perform(get("/mock/api/inventory")
                .param("skuCode", "LOW-product")
                .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void isInStock_withLowProductAndLowQuantity_shouldReturnTrue() throws Exception {
        mockMvc.perform(get("/mock/api/inventory")
                .param("skuCode", "LOW-product")
                .param("quantity", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
} 