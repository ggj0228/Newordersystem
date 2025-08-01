package com.example.ordersystem.member.service;


import com.example.ordersystem.common.service.SseEmitterRegistry;
import com.example.ordersystem.member.dto.SseDtoMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
public class SseAlarmService implements MessageListener {

    private final SseEmitterRegistry sseEmitterRegistry;
    private final RedisTemplate<String, String> redisTemplate;

    public SseAlarmService(SseEmitterRegistry sseEmitterRegistry, @Qualifier("ssePubSub") RedisTemplate<String, String> redisTemplate) {
        this.sseEmitterRegistry = sseEmitterRegistry;
        this.redisTemplate = redisTemplate;
    }

    // 특정 사용자에게 메시지 발송
    public void publishMessage(String receiver, String sender, Long orderingId) {
        SseDtoMessage dto = SseDtoMessage.builder()
                .receiver(receiver)
                .sender(sender)
                .orderingId(orderingId)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String data = null;
        try {
            data = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // emitter 객체를 통해 메시지 전송
        SseEmitter sseEmitter = sseEmitterRegistry.getEmitter(receiver);
        // emitter객체가 현재 서버에 있으면, 직접 알림 발송. 그렇지 않으면, redis에 publish
        if(sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("ordered").data(data));
                // 사용자가 로그아웃 후에 다시 화면에 들어왓을 때 알림메시지가 남아있으면 DB에 추가적으로 저장 필요.
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(sseEmitter == null) {
            redisTemplate.convertAndSend("order-channel", data);
        }
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Message: 실질적인 메시지가 담겨있는 객체
        // Pattern: 채널명
        ObjectMapper objectMapper = new ObjectMapper();
        // 여러개의 채널을 구독하고 있을 경우, 채널명으로 분기처리ㅋ
        try {
            SseDtoMessage dto = objectMapper.readValue(message.getBody(), SseDtoMessage.class);
            SseEmitter sseEmitter = sseEmitterRegistry.getEmitter(dto.getReceiver());
            // emitter객체가 현재 서버에 있으면, 직접 알림 발송. 그렇지 않으면, redis에 publish
            if(sseEmitter != null) {
                try {
                    sseEmitter.send(SseEmitter.event().name("ordered").data(dto));
                    // 사용자가 로그아웃 후에 다시 화면에 들어왓을 때 알림메시지가 남아있으면 DB에 추가적으로 저장 필요.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
