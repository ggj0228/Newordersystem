package com.example.ordersystem.member.controller;

import com.example.ordersystem.common.dto.response.CommonCorrectResponse;
import com.example.ordersystem.member.dto.MemberCreateDto;
import com.example.ordersystem.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign")
    public ResponseEntity<?> signUp(@Valid @RequestBody MemberCreateDto dto) {
        Long id = this.memberService.signUp(dto);
       return  new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(id)
                .status_code(HttpStatus.CREATED.value())
                .status_message("회원가입 완료")
                .build(),
               HttpStatus.CREATED);
    }
}
