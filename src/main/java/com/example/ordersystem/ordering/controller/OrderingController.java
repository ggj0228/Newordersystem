package com.example.ordersystem.ordering.controller;

import com.example.ordersystem.common.dto.response.CommonCorrectResponse;
import com.example.ordersystem.ordering.domain.Ordering;
import com.example.ordersystem.ordering.dto.OrderingCreateDto;
import com.example.ordersystem.ordering.dto.OrderingResponseDto;
import com.example.ordersystem.ordering.service.OrderingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderingController {

    private final OrderingService orderingService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody List<OrderingCreateDto> orderCreateDtos) {
        OrderingResponseDto result = this.orderingService.createOrderCuncurrent(orderCreateDtos);
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(result)
                .status_code(HttpStatus.CREATED.value())
                .status_message("주문 완료")
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(this.orderingService.findAll())
                .status_code(HttpStatus.OK.value())
                .status_message("주문 리스트")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/myorders")
    public ResponseEntity<?> myOrders() {
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(this.orderingService.findAllByMember())
                .status_code(HttpStatus.OK.value())
                .status_message("내 주문 목록 조회")
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> orderCancel (@PathVariable Long id) {
        Ordering ordering = orderingService.cancel(id);
    return new ResponseEntity<>(CommonCorrectResponse.builder()
            .response(id)
            .status_code(HttpStatus.OK.value())
            .status_message("주문취소 완효")
            .build(), HttpStatus.OK);

    }

}
