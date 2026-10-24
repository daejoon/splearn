package com.ddoong2.splearn.domain.member;

import com.ddoong2.splearn.application.member.provided.MemberRegisterRequest;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {
    public static Member createMember() {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
        return member;
    }

    public static Member createMember(Long memberId) {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }

    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "daejoon", "very_long_secret");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("kkode1911@gmail.com");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

    public static Member createActiveMember() {
        Member member = createMember();
        member.activate();
        return member;
    }
}
