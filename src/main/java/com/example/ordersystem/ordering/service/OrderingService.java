package com.example.ordersystem.ordering.service;


import com.example.ordersystem.common.service.StockInventoryService;
import com.example.ordersystem.common.service.StockRabbitMqService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    private final StockInventoryService stockInventoryService;
    private final StockRabbitMqService  stockRabbitMqService;


    public OrderingResponseDto createOrder(List<OrderingCreateDto> dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        Ordering ordering = Ordering.builder().member(member).build();
        List<OrderingFailedDto> orderingFailedDtos = new ArrayList<>();
        int idx = 0;
        for (OrderingCreateDto orderCreateDto : dto) {
            Product product = productRepository.findById(dto.get(idx).getProductId()).orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
            if (product.getStockQuantity() < dto.get(idx).getProductCount()) {
                throw new IllegalArgumentException("현재 재고에서" + (dto.get(idx).getProductCount() - product.getStockQuantity()) + "개의 상품이 없습니다.");
            }
            // 1. 동시에 접근하는 상황에서 update값의 정합성이 깨지고 갱신이상이 발생
            // 2. Spring버전이나 myssql버전에 따라 jpa에서 강제에러(deadlock)를 유발시켜 대부분의 요청실패 발생
            int productAfterStockQuantity = product.getStockQuantity() - dto.get(idx).getProductCount();
            product.updateStockQuantity(productAfterStockQuantity);
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .ordering(ordering)
                    .productCount(dto.get(idx).getProductCount())
                    .totalPrice(product.getPrice() * dto.get(idx).getProductCount())
                    .build();
            ordering.getOrderDetailList().add(orderDetail);

            idx++;
        }
        this.orderingRepository.save(ordering);
        OrderingResponseDto response = OrderingResponseDto.fromEntity(ordering, OrderStatus.ORDERED);
        return response;
//        }
//        if (!ordering.getOrderDetailList().isEmpty()) {
//            this.orderingRepository.save(ordering);
//            OrderingResponseDto response = OrderingResponseDto.fromEntity(ordering, OrderStatus.ORDERED);
//            return new OrderingResultDto(response, orderingFailedDtos);
//        } else {
//            // 모든 상품이 실패한 경우
//            return new OrderingResultDto(null, orderingFailedDtos);
//        }
    }

    // redis에 연동
    @Transactional(isolation = Isolation.READ_COMMITTED)// 격리레벨을 낮춤으로서, 성능향상과 lock관련 문제 원천 차단
    public OrderingResponseDto createOrderCuncurrent(List<OrderingCreateDto> dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        Ordering ordering = Ordering.builder().member(member).build();
        List<OrderingFailedDto> orderingFailedDtos = new ArrayList<>();
        int idx = 0;
        for (OrderingCreateDto orderCreateDto : dto) {
            Product product = productRepository.findById(dto.get(idx).getProductId()).orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));
                // redis에서 재고수량 확인및 재고수량 감소 처리
              int newQuantity =  stockInventoryService.decreaseStockQuntity(dto.get(idx).getProductId(), dto.get(idx).getProductCount());
              if(newQuantity < 0) {
                  throw new IllegalArgumentException("재고 부족");
              }

            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .ordering(ordering)
                    .productCount(dto.get(idx).getProductCount())
                    .totalPrice(product.getPrice() * dto.get(idx).getProductCount())
                    .build();
            ordering.getOrderDetailList().add(orderDetail);
//            rdb에 사후 update를 위한 메시지 발행 (비동기 처리)
            stockRabbitMqService.publish(orderCreateDto.getProductId(), orderCreateDto.getProductCount());
            idx++;

        }
        this.orderingRepository.save(ordering);
        OrderingResponseDto response = OrderingResponseDto.fromEntity(ordering, OrderStatus.ORDERED);
        return response;
    }


    public List<OrderListDto> findAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        List<OrderListDto> orderListDtos = new ArrayList<>();
        List<Ordering> orderings = orderingRepository.findAll();
        for (Ordering ordering : orderings) {
            orderListDtos.add(OrderListDto.fromEntity(ordering));
        }
        return orderListDtos;
    }


    public List<OrderingMySelfListDto> findAllByMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
//        List<OrderingMySelfListDto> orderLists = new ArrayList<>();
//        List<Ordering> orderings = this.orderingRepository.findAllByMember(member);
//        for(Ordering ordering : orderings) {
//            orderLists.add(OrderingMySelfListDto.fromEntity(ordering));
        List<OrderingMySelfListDto> orderLists = orderingRepository.findAllByMember(member).stream().map(a -> OrderingMySelfListDto.fromEntity(a)).toList();
        return orderLists;
    }


    public Ordering cancel (Long id) {
        Ordering ordering = this.orderingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("order id가 없습니다."));

        // Ordering DB에 상태값만 변경
        ordering.cancelstatus(OrderStatus.CANCLED);
        // redis의 재고값 증가
        for(OrderDetail detail :ordering.getOrderDetailList()) {
            detail.getProduct().cancelOrder(detail.getProductCount());
            this.stockInventoryService.inCreaseStockQuantity(detail.getProduct().getId(), detail.getProductCount());

        }
        return ordering;
    }
}
