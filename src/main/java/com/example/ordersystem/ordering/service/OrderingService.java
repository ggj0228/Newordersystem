package com.example.ordersystem.ordering.service;


import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.repository.MemeberRepository;
import com.example.ordersystem.orderdetail.domain.OrderDetail;
import com.example.ordersystem.orderdetail.repository.OrderDetailRepository;
import com.example.ordersystem.ordering.domain.OrderStatus;
import com.example.ordersystem.ordering.domain.Ordering;
import com.example.ordersystem.ordering.dto.*;
import com.example.ordersystem.ordering.repository.OrderingRepository;
import com.example.ordersystem.product.domain.Product;
import com.example.ordersystem.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderingService {
    private final OrderingRepository orderingRepository;
    private final MemeberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderingResultDto createOrder(List<OrderingCreateDto> dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        Ordering ordering = Ordering.builder().member(member).build();
        List<OrderingFailedDto> orderingFailedDtos = new ArrayList<>();
        int idx = 0;
        for (OrderingCreateDto orderCreateDto : dto) {
            Product product = productRepository.findById(dto.get(idx).getProductId()).orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
            if (product.getStockQuantity() < dto.get(idx).getProductCount()) {
                orderingFailedDtos.add(new OrderingFailedDto(product, "현재 재고에서" + (dto.get(idx).getProductCount() - product.getStockQuantity()) + "개의 상품이 없습니다."));
                continue;

            }
                int productAfterStockQuantity = product.getStockQuantity() - dto.get(idx).getProductCount();
                product.updateStockQuantity(productAfterStockQuantity);
                productRepository.save(product);

                OrderDetail orderDetail = OrderDetail.builder()
                        .product(product)
                        .ordering(ordering)
                        .productCount(dto.get(idx).getProductCount())
                        .totalPrice(product.getPrice() * dto.get(idx).getProductCount())
                        .build();
                ordering.getOrderDetailList().add(orderDetail);

                idx++;

        }
        if (!ordering.getOrderDetailList().isEmpty()) {
            this.orderingRepository.save(ordering);
            OrderingResponseDto response = OrderingResponseDto.fromEntity(ordering, OrderStatus.ORDERED);
            return new OrderingResultDto(response, orderingFailedDtos);
        } else {
            // 모든 상품이 실패한 경우
            return new OrderingResultDto(null, orderingFailedDtos);
        }
    }

    public List<OrderListDto> findAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        List<OrderListDto> orderListDtos = new ArrayList<>();
        List<Ordering> orderings = orderingRepository.findAllByMember(member);
        for (Ordering ordering : orderings) {
            orderListDtos.add(OrderListDto.fromEntity(ordering));
        }
        return orderListDtos;
    }
}
