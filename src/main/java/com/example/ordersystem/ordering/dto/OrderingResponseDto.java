package com.example.ordersystem.ordering.dto;


import com.example.ordersystem.orderdetail.dto.OrderDetailDto;
import com.example.ordersystem.ordering.domain.OrderStatus;
import com.example.ordersystem.ordering.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderingResponseDto {
    private Long orderingId;
    private String memberEmail;
    private String memberName;
    private OrderStatus orderStatus;
    private List<OrderDetailDto> orderDetails;

    public static OrderingResponseDto fromEntity(Ordering ordering, OrderStatus orderStatus) {
        return OrderingResponseDto.builder()
                .orderingId(ordering.getId())
                .memberEmail(ordering.getMember().getEmail())
                .memberName(ordering.getMember().getName())
                .orderStatus(orderStatus)
                .orderDetails(ordering.getOrderDetailList().stream()
                        .map(a -> OrderDetailDto.fromEntity(a))
                        .collect(Collectors.toList()))
                .build();
    }
}