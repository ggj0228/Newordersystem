package com.example.ordersystem.product.service;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.repository.MemeberRepository;
import com.example.ordersystem.product.domain.Product;
import com.example.ordersystem.product.dto.ProductCreateDto;
import com.example.ordersystem.product.dto.ProductDetailDto;
import com.example.ordersystem.product.dto.ProductListDto;
import com.example.ordersystem.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemeberRepository memberRepository;


    public void createProduct(ProductCreateDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        productRepository.save(dto.toEntity(member));
    }

    public List<ProductListDto> findAll() {
        return productRepository.findAll().stream().map(a -> ProductListDto.fromEntity(a)).toList();
    }

    public ProductDetailDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
        return ProductDetailDto.fromEntity(product);
    }
}
