package com.example.ordersystem.member.domain;

import com.example.ordersystem.common.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    private String delYn;

    @Column(nullable = true)
    private LocalDateTime deletedTime;
    // 생성일자도 있음

    public void delete() {
        this.delYn = "Y";
        this.deletedTime = LocalDateTime.now();
    }

}
