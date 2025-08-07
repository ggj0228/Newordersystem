package com.example.ordersystem.product.controller;

import com.example.ordersystem.common.dto.response.CommonCorrectResponse;
import com.example.ordersystem.product.dto.ProductCreateDto;
import com.example.ordersystem.product.dto.ProductResDto;
import com.example.ordersystem.product.dto.ProductSearchDto;
import com.example.ordersystem.product.dto.ProductUpdateDto;
import com.example.ordersystem.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductCreateDto dto,
                                           @RequestParam(name ="productImage", required = false) MultipartFile productImage) {
        Long id = this.productService.createProduct(dto, productImage);
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(id)
                .status_code(HttpStatus.CREATED.value())
                .status_message("상품 등록 완료")
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @ModelAttribute ProductUpdateDto dto) {
        Long productId =  this.productService.updateProduct(id, dto);
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(productId)
                .status_code(HttpStatus.OK.value())
                .status_message("상품 수정 완료")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll(Pageable pageable, ProductSearchDto dto) {

        return new ResponseEntity<> (CommonCorrectResponse.builder()
                .response(this.productService.findAll(pageable, dto))
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
