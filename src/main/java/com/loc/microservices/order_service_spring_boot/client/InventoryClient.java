package com.loc.microservices.order_service_spring_boot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "inventory", url = "${inventory.service.url}")
public interface InventoryClient {


    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @RequestMapping(method = RequestMethod.GET, value = "/api/inventory")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    boolean isInStock(@RequestParam String skuCode,@RequestParam Integer quantity );

    default boolean fallbackMethod(String skuCode, Integer quantity, Exception exception){
        log.info("Cannot get inventory for skuCode: {}, quantity: {}, exception: {}", skuCode, quantity, exception.getMessage());
        return false;
    }
}
