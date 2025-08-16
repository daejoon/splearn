package com.ddoong2.splearn.application;

import com.ddoong2.splearn.application.provided.MemberRegister;
import com.ddoong2.splearn.application.required.EmailSender;
import com.ddoong2.splearn.application.required.MemberRepository;
import com.ddoong2.splearn.domain.DuplicateEmailException;
import com.ddoong2.splearn.domain.Email;
import com.ddoong2.splearn.domain.Member;
import com.ddoong2.splearn.domain.MemberRegisterRequest;
import com.ddoong2.splearn.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일 입니다: " + registerRequest.email());
        }
    }
}
