package com.example.ordersystem.product.domain;

import com.example.ordersystem.common.domain.BaseTime;
import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.product.dto.ProductUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private int price;
    private int stockQuantity;
    private String productImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    public void updateImageUrl(String profileImageUrl) {
        this.productImage = profileImageUrl;
    }
    public void updateProduct(ProductUpdateDto dto) {
        this.name = dto.getName();
        this.category = dto.getCategory();
        this.price = dto.getPrice();
        this.stockQuantity = dto.getStockQuantity();
    }

    public void updateStockQuantity(int quantity) {
        this.stockQuantity -= quantity;
    }

    public void cancelOrder (int quantity) {
        this.stockQuantity += quantity;
    }
}
