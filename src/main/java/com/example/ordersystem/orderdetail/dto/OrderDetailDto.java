package com.example.ordersystem.orderdetail.dto;

import com.example.ordersystem.orderdetail.domain.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto {
    private Long productId;
    private String productName;
    private int productCount;
    private int totalPrice;

    public static OrderDetailDto fromEntity(OrderDetail orderDetail) {
        return OrderDetailDto.builder()
                .productId(orderDetail.getProduct().getId())
                .productName(orderDetail.getProduct().getName())
                .productCount(orderDetail.getProductCount())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }
}