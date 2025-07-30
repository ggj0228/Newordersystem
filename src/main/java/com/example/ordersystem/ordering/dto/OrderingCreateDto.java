package com.example.ordersystem.ordering.dto;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.ordering.domain.OrderStatus;
import com.example.ordersystem.ordering.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderingCreateDto {
    private Long productId;
    private int productCount;

    public static Ordering toEntity(Member member, OrderStatus orderStatus) {
        return Ordering.builder()
                .member(member)
                .orderStatus(orderStatus)
                .build();
    }
}