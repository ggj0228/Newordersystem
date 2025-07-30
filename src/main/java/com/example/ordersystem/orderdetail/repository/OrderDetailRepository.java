package com.example.ordersystem.orderdetail.repository;

import com.example.ordersystem.orderdetail.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

//    List<OrderDetail> finAllByOrdering(Ordering ordering);
}
