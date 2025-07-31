package com.example.ordersystem.common.service;

import com.example.ordersystem.common.dto.StockRabbitMqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockRabbitMqService {
    private final RabbitTemplate rabbitTemplate;

    //rabbitmq에 메시지 발행
    public void publish(Long productId, int productCount){
        StockRabbitMqDto dto = StockRabbitMqDto.builder()
                .productId(productId)
                .productCount(productCount)
                .build();
        rabbitTemplate.convertAndSend("stockDecreaseQueue", dto);
    }
    // rabbitmq에서 메시지 수신
}
