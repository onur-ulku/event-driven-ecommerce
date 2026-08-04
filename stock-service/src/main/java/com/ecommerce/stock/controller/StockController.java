package com.ecommerce.stock.controller;

import com.ecommerce.stock.dto.StockResponse;
import com.ecommerce.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{productId}")
    public ResponseEntity<StockResponse> getStock(@PathVariable String productId) {
        return ResponseEntity.ok(stockService.getStock(productId));
    }
}
