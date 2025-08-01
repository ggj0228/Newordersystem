package com.example.ordersystem.common.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

@Component
public class SseEmitterRegistry {
    // SseEmitter는 연결된 사용자 정보를 의미(ip, 등)
    // ConcurrentMap은 thread-safe한 map (동시성 이슈 x)
    private Map<String, SseEmitter> emitterMap = new ConcurrentMap<String, SseEmitter>() {
        @Override
        public SseEmitter putIfAbsent(String key, SseEmitter value) {
            return null;
        }

        @Override
        public boolean remove(Object key, Object value) {
            return false;
        }

        @Override
        public boolean replace(String key, SseEmitter oldValue, SseEmitter newValue) {
            return false;
        }

        @Override
        public SseEmitter replace(String key, SseEmitter value) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public SseEmitter get(Object key) {
            return null;
        }

        @Override
        public SseEmitter put(String key, SseEmitter value) {
            return null;
        }

        @Override
        public SseEmitter remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends SseEmitter> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<String> keySet() {
            return Set.of();
        }

        @Override
        public Collection<SseEmitter> values() {
            return List.of();
        }

        @Override
        public Set<Entry<String, SseEmitter>> entrySet() {
            return Set.of();
        }
    };

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
