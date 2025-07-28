package com.example.ordersystem.member.domain;

import com.example.ordersystem.common.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
// jpql을 제외하고 모든 조회쿼리에 where del_yn = 'N'을 붙이는 효과
@Where(clause = "del_yn = 'N'")
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
