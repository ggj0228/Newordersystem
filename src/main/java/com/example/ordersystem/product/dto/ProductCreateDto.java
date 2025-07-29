package com.example.ordersystem.product.dto;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductCreateDto {
    private String name;
    private String category;
    private int price;
    private int stockQuantity;

    public Product toEntity(Member member) {
        return Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .stockQuantity(stockQuantity)
                .member(member)
                .build();
    }
}
