package com.example.ordersystem.orderdetail.domain;

import com.example.ordersystem.common.domain.BaseTime;
import com.example.ordersystem.ordering.domain.Ordering;
import com.example.ordersystem.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

public class OrderDetail extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private int productCount;
    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordering_id")
    Ordering ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;


}
