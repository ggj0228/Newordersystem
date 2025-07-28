package com.example.ordersystem.common.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter

public class BaseTime {

    @CreationTimestamp
    private LocalDateTime createdTime;
}
