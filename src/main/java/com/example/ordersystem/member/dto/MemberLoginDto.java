package com.example.ordersystem.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class MemberLoginDto {
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Size(min = 8, message = "8자리 이상 입력해주세요")
    private String password;
}
