package com.example.ordersystem.ordering.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderingResultDto {
    private OrderingResponseDto success;
    private List<OrderingFailedDto> fail;

    public OrderingResultDto(OrderingResponseDto orderingResponseDto, List<OrderingFailedDto> orderingFailedDtos) {
        this.success = orderingResponseDto;
        this.fail = orderingFailedDtos;

    }
}
