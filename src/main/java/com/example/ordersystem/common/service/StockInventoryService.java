package com.example.ordersystem.common.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockInventoryService {

    private final RedisTemplate<String, String> redisTemplate;

    public StockInventoryService(@Qualifier("stockInventory")RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    //상품 등록시 재고수량 세팅
    public void makeStockQuntity(Long productId, int quantity) {
         redisTemplate.opsForValue().set(productId.toString(), String.valueOf(quantity));
    }
    // 주문 성공시 재고수량 감소
    public int decreaseStockQuntity(Long productId, int orderQuantity) {
        String remainobject = redisTemplate.opsForValue().get(String.valueOf(productId));
        int remains = Integer.parseInt(remainobject);
        if(remains < orderQuantity) {
            return -1;
        } else {
            Long finalRemains = redisTemplate.opsForValue().decrement(String.valueOf(productId), orderQuantity);
            return finalRemains.intValue();
        }
    }
    // 주문 취소시 재고수량 증가
    public int inCreaseStockQuantity(Long productId, int quantity){
        Long finalRemains =redisTemplate.opsForValue().increment(String.valueOf(productId), quantity);
        return finalRemains.intValue();
        }
}
