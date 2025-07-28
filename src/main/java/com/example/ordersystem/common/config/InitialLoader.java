package com.example.ordersystem.common.config;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.domain.Role;
import com.example.ordersystem.member.repository.MemeberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component

public class InitialLoader implements CommandLineRunner {
    private final MemeberRepository memeberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String email;
    @Value("${admin.name}")
    private String name;
    @Value("${admin.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        if(this.memeberRepository.findByEmail(email).isPresent()) {
            return;
        }
        Member member = Member.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .delYn("N")
                .build();

        this.memeberRepository.save(member);
    }
}
