package com.ddoong2.splearn.application.member;

import com.ddoong2.splearn.application.member.provided.MemberRegister;
import com.ddoong2.splearn.application.member.required.EmailSender;
import com.ddoong2.splearn.application.member.required.MemberRepository;
import com.ddoong2.splearn.domain.member.DuplicateEmailException;
import com.ddoong2.splearn.domain.member.DuplicateProfileException;
import com.ddoong2.splearn.domain.member.Member;
import com.ddoong2.splearn.domain.member.MemberInfoUpdateRequest;
import com.ddoong2.splearn.domain.member.MemberRegisterRequest;
import com.ddoong2.splearn.domain.member.PasswordEncoder;
import com.ddoong2.splearn.domain.member.Profile;
import com.ddoong2.splearn.domain.shared.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
    private final MemberQueryService memberQueryService;
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

    @Override
    public Member activate(Long memberId) {
        Member member = memberQueryService.find(memberId);

        member.activate();

        return memberRepository.save(member);
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberQueryService.find(memberId);

        member.deactivate();

        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberQueryService.find(memberId);

        checkDuplicateProfile(member, memberInfoUpdateRequest.profileAddress());

        member.updateInfo(memberInfoUpdateRequest);

        return memberRepository.save(member);
    }

    private void checkDuplicateProfile(Member member, String profileAddress) {
        if (profileAddress.isEmpty()) return;

        Profile currentProfile = member.getDetail().getProfile();
        if (currentProfile != null && currentProfile.address().equals(profileAddress)) return;

        if (memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
            throw new DuplicateProfileException("이미존재하는 프로필 입니다 Profile: " + profileAddress);
        }
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
