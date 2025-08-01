package com.example.ordersystem.member.service;


import com.example.ordersystem.member.dto.SseDtoMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SseAlarmService {
    // SseEmitter는 연결된 사용자 정보를 의미(ip, 등)
    private Map<String, SseEmitter> emitterMap = new HashMap();

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
        SseEmitter sseEmitter = emitterMap.get(receiver);
        try {
            sseEmitter.send(SseEmitter.event().name("ordered").data(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 사용자가 로그아웃 후에 다시 화면에 들어왓을 때 알림메시지가 남아있으ㅕㄴ DB에 추가적으로 저장 필요.


    public void addSseEmitter(String email, SseEmitter sseEmitter) {
//        SseEmitter sseEmitter = new SseEmitter(14400*60*1000L); // 10일 정도 emitter 유효기간 설정
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        emitterMap.put(email, sseEmitter);
        emitterMap.put(email, sseEmitter);
        System.out.println(emitterMap);
    }
}
