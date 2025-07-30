//package com.example.ordersystem.orderdetail.service;
//
//import com.example.ordersystem.orderdetail.repository.OrderDetailRepository;
//import com.example.ordersystem.ordering.repository.OrderingRepository;
//import com.example.ordersystem.product.repository.ProductRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
////@Service
////@Transactional
////@RequiredArgsConstructor
////public class OrderDetailService {
////    private final OrderDetailRepository orderRepository;
////    private final OrderingRepository orderingRepository;
////    private final ProductRepository productRepository;
////
//
////    public OrderCreateDto createOrder(OrderCreateDto orderCreateDto) {
////        String email = SecurityContextHolder.getContext().getAuthentication().getName();
////        Member member = this.orderingRepository.finByMemberEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
////
////        orderRepository.save(orderCreateDto.toEntity(product, ordering));
////        return orderCreateDto;
////    }
//}
