package com.example.ordersystem.ordering.dto;

import com.example.ordersystem.orderdetail.dto.OrderDetailDto;
import com.example.ordersystem.ordering.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class OrderListDto {
    private Long memberId;
    private String memberEmail;
    private String memberName;
    private String orderStatus;
    private List<OrderDetailDto> orderDetails;

    public static OrderListDto fromEntity(Ordering ordering) {
        return OrderListDto.builder()
                .memberId(ordering.getMember().getId())
                .memberEmail(ordering.getMember().getEmail())
                .memberName(ordering.getMember().getName())
                .orderStatus(ordering.getOrderStatus().toString())
                .orderDetails(ordering.getOrderDetailList().stream()
                        .map(a -> OrderDetailDto.fromEntity(a))
                        .toList())
                .build();
    }
}
