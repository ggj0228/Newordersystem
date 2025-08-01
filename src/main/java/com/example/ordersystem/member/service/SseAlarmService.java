package com.example.ordersystem.member.service;


import com.example.ordersystem.common.service.SseEmitterRegistry;
import com.example.ordersystem.member.dto.SseDtoMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SseAlarmService {

    private final SseEmitterRegistry sseEmitterRegistry;

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
        try {
            sseEmitter.send(SseEmitter.event().name("ordered").data(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 사용자가 로그아웃 후에 다시 화면에 들어왓을 때 알림메시지가 남아있으ㅕㄴ DB에 추가적으로 저장 필요.



}
