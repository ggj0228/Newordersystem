package com.example.ordersystem.member.dto;

import com.example.ordersystem.member.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MemberCreateDto {
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Size(min = 8, message = "8자리 이상 입력해주세요")
    private String password;
    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .name(this.name)
                .password(encodedPassword)
                .email(this.email)
                .delYn("N")
                .build();
    }
}
