package com.example.ordersystem.product.controller;

import com.example.ordersystem.common.dto.response.CommonCorrectResponse;
import com.example.ordersystem.product.dto.ProductCreateDto;
import com.example.ordersystem.product.dto.ProductResDto;
import com.example.ordersystem.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductCreateDto dto) {
        Long id = this.productService.createProduct(dto);
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(id)
                .status_code(HttpStatus.CREATED.value())
                .status_message("상품 등록 완료")
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll() {
        List<ProductResDto> dto = this.productService.findAll();
        return new ResponseEntity<> (CommonCorrectResponse.builder()
                .response(dto)
                .status_code(HttpStatus.OK.value())
                .status_message("상품 리스트")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProductResDto dto = this.productService.findById(id);
        return new ResponseEntity<> (CommonCorrectResponse.builder()
                .response(dto)
                .status_code(HttpStatus.OK.value())
                .status_message("상품 상세보기")
                .build(), HttpStatus.OK);
    }
}
