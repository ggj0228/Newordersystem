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
public class OrderingMySelfListDto {
    private String memberName;
    private String memberEmail;
    private String orderStatus;
    private List<OrderDetailDto> orderDetails;

    public static OrderingMySelfListDto fromEntity(Ordering ordering) {
        return OrderingMySelfListDto.builder()
                .memberName(ordering.getMember().getName())
                .memberEmail(ordering.getMember().getEmail())
                .orderStatus(ordering.getOrderStatus().toString())
                .orderDetails(ordering.getOrderDetailList().stream()
                        .map(a -> OrderDetailDto.fromEntity(a))
                        .toList())
                .build();
    }
}
