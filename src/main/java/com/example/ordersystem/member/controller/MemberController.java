package com.example.ordersystem.member.controller;

import com.example.ordersystem.common.Auth.JwtTokenProvider;
import com.example.ordersystem.common.dto.response.CommonCorrectResponse;
import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.dto.*;
import com.example.ordersystem.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberLoginDto dto) {
        Member member = this.memberService.login(dto);
        // at 토큰 생성
        String accessToken = jwtTokenProvider.createAtToken(member);
        // rt토큰 생성
        String refreshToken = jwtTokenProvider.createhRtToken(member);

        MemberLoginResDto memberLoginResDto = MemberLoginResDto.builder()
                .accessToken(accessToken)
                .RefreshToken(refreshToken)
                .build();
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(memberLoginResDto)
                .status_code(HttpStatus.OK.value())
                .status_message("로그인 성공")
                .build(), HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete (@Valid @RequestBody MemberDeleteDto dto) {
        this.memberService.delete(dto);
        return new ResponseEntity<>("회원탈퇴 완료", HttpStatus.OK);
    }


    // rt를 통한 at 갱신 요청
    @PostMapping("/refresh-at")
    public ResponseEntity<?> generateNewAt(@RequestBody RefreshTokenDto dto) {
        // rt검증 로직
        Member member = jwtTokenProvider.validateRefreshToken(dto.getRefreshToken());
        // at신규 생성
        String newAccessToken = jwtTokenProvider.createAtToken(member);
        MemberLoginResDto memberLoginResDto = MemberLoginResDto.builder()
                .accessToken(newAccessToken)
                .RefreshToken(dto.getRefreshToken())
                .build();
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(memberLoginResDto)
                .status_code(HttpStatus.OK.value())
                .status_message("로그인 성공")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> memberList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
       Page<MemberResDto> members =  this.memberService.findall(pageable);
       return new ResponseEntity<>(CommonCorrectResponse.builder()
               .response(members)
               .status_code(HttpStatus.OK.value())
               .status_message("멤버 리스트")
               .build(), HttpStatus.OK);
    }

    @GetMapping("/myinfo")
    public ResponseEntity<?> myinfo() {
        MemberResDto memberResDto = this.memberService.myinfo();
        return new ResponseEntity<>(CommonCorrectResponse.builder()
                .response(memberResDto)
                .status_code(HttpStatus.OK.value())
                .status_message("내 정보")
                .build(), HttpStatus.OK);
    }
}
