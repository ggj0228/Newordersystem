package com.example.ordersystem.ordering.domain;

import com.example.ordersystem.common.domain.BaseTime;
import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.orderdetail.domain.OrderDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder

public class Ordering extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @OneToMany(mappedBy = "ordering", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Builder.Default
    List<OrderDetail> orderDetailList = new ArrayList<>();

    public  void cancelstatus(OrderStatus status) {
        this.orderStatus = status;
    }


}
