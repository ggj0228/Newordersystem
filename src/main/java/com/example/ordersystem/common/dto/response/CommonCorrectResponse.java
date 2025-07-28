package com.example.ordersystem.common.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommonCorrectResponse {
    private Object response;
    private int status_code;
    private String status_message;
}
