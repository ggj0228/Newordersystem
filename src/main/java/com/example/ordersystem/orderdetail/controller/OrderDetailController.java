//package com.example.ordersystem.orderdetail.controller;
//
//import com.example.ordersystem.orderdetail.service.OrderDetailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/order")
//@RequiredArgsConstructor
//public class OrderDetailController {
//    private final OrderDetailService orderService;
//
//    @PostMapping("/create")
//    public ResponseEntity<?> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
//        this.orderService.createOrder(orderCreateDto);
//        return null;
//    }
//}
