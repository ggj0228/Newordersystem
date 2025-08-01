package com.example.ordersystem.common.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRegistry {
    // SseEmitter는 연결된 사용자 정보를 의미(ip, 등)
    // ConcurrentHashMap은 thread-safe한 map (동시성 이슈 x)
    private Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter getEmitter(String email) {
        return emitterMap.get(email);
    }

    public void addSseEmitter(String email, SseEmitter sseEmitter) {
//        SseEmitter sseEmitter = new SseEmitter(14400*60*1000L); // 10일 정도 emitter 유효기간 설정
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        emitterMap.put(email, sseEmitter);
        emitterMap.put(email, sseEmitter);
        System.out.println(emitterMap);
    }

    public void removeEmitter(String email) {
        emitterMap.remove(email);
    }

}
