package com.example.ordersystem.product.service;

import com.example.ordersystem.member.repository.MemeberRepository;
import com.example.ordersystem.product.dto.ProductCreateDto;
import com.example.ordersystem.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemeberRepository memberRepository;


    public void createProduct(ProductCreateDto dto) {
        productRepository.save(dto.toEntity());
    }
}
