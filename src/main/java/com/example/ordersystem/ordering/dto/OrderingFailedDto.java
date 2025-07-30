package com.example.ordersystem.ordering.dto;

import com.example.ordersystem.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderingFailedDto {

    private Long productId;
    private String productName;
    private String reason;

    public OrderingFailedDto(Product product, String reason) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.reason = reason;
    }
}
