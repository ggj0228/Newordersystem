package com.example.ordersystem.ordering.controller;

import com.example.ordersystem.common.dto.response.CommonCorrectResponse;
import com.example.ordersystem.ordering.dto.OrderingCreateDto;
import com.example.ordersystem.ordering.dto.OrderingResultDto;
import com.example.ordersystem.ordering.service.OrderingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderingController {

    private final OrderingService orderingService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody List<OrderingCreateDto> orderCreateDtos) {
        OrderingResultDto result = this.orderingService.createOrder(orderCreateDtos);
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(result)
                .status_code(HttpStatus.CREATED.value())
                .status_message("주문 완료")
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(this.orderingService.findAll())
                .status_code(HttpStatus.OK.value())
                .status_message("주문 리스트")
                .build(), HttpStatus.OK);
    }
}
