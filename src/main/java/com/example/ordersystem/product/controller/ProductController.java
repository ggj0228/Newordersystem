package com.example.ordersystem.product.controller;

import com.example.ordersystem.common.dto.response.CommonCorrectResponse;
import com.example.ordersystem.product.dto.ProductCreateDto;
import com.example.ordersystem.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateDto dto) {
        this.productService.createProduct(dto);
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(dto)
                .status_code(HttpStatus.CREATED.value())
                .status_message("생성 완료")
                .build(), HttpStatus.CREATED);
    }
}
