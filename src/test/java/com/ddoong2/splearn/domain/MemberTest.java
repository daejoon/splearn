package com.ddoong2.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

        member = Member.register(new MemberRegisterRequest("kkode1911@gmail.com", "daejoon", "secret"), passwordEncoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("멤버를 활성화 한다")
    void _멤버를_활성화_한다() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }

    @Test
    @DisplayName("멤버 활성화 실패")
    void _멤버_활성화_실패() {
        member.activate();

        assertThatThrownBy(() -> {
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("가입완료 상태에서 탈퇴할수 있다")
    void _가입완료_상태에서_탈퇴할수_있다() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATE);
    }

    @Test
    void deactivateFail() {
        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("비밀번호를 확인")
    void _비밀번호를_확인() {
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    @DisplayName("닉네임 변경")
    void _닉네임_변경() {
        assertThat(member.getNickname()).isEqualTo("daejoon");

        member.changeNickname("hoho");

        assertThat(member.getNickname()).isEqualTo("hoho");
    }

    @Test
    @DisplayName("비밀번호 변경")
    void _비밀번호_변경() {
        member.changePassword("verysecret", passwordEncoder);

        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }

    @Test
    @DisplayName("상태체크")
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    @DisplayName("이메일주소 검증")
    void _이메일주소_검증() {
        assertThatThrownBy(() -> {
            Member.register(new MemberRegisterRequest("Invalid Email", "daejoon", "secret"), passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);

        Member.register(new MemberRegisterRequest("kkode1911@gmail.com", "daejoon", "secret"), passwordEncoder);
    }
}