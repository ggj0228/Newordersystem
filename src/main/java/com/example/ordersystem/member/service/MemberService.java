package com.example.ordersystem.member.service;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.dto.MemberCreateDto;
import com.example.ordersystem.member.dto.MemberLoginDto;
import com.example.ordersystem.member.repository.MemeberRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemeberRepository memeberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signUp(MemberCreateDto dto) {
       if(this.memeberRepository.findByEmail(dto.getEmail()).isPresent()){
           throw new EntityExistsException("이메일 존재");
       }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
       Member member = this.memeberRepository.save(dto.toEntity(encodedPassword));
       Long id = member.getId();
       return id;

    }

    public Member login(MemberLoginDto dto) {
        Optional<Member> optionalMember = this.memeberRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if(!optionalMember.isPresent()){
            check = false;
        } else {
            if(!passwordEncoder.matches(dto.getPassword(), optionalMember.get().getPassword())){
                check = false;
            }
        }

        if(!check) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀립니다.");
        }
        return optionalMember.get();
    }
}
