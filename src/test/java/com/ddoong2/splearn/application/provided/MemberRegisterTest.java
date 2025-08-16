package com.ddoong2.splearn.application.provided;

import com.ddoong2.splearn.SplearnTestConfiguration;
import com.ddoong2.splearn.domain.DuplicateEmailException;
import com.ddoong2.splearn.domain.Member;
import com.ddoong2.splearn.domain.MemberFixture;
import com.ddoong2.splearn.domain.MemberRegisterRequest;
import com.ddoong2.splearn.domain.MemberStatus;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    @DisplayName("스프링부트 등록테스트")
    void _등록테스트() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("이메일 중복 실패")
    void _이메일_중복_실패() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThatThrownBy(() -> {
            memberRegister.register(MemberFixture.createMemberRegisterRequest());
        }).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("activate")
    void _activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member actual = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(actual.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }

    @Test
    @DisplayName("memberRegisterRequestFail")
    void _memberRegisterRequestFail() {

        validated(new MemberRegisterRequest("fail@gmail.com", "1234", "long_secret"));
        validated(new MemberRegisterRequest("fail@gmail.com", "daejoon_________", "secret"));
        validated(new MemberRegisterRequest("failgmail.com", "daejoon_________", "long_secret"));
    }

    private void validated(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> {
            memberRegister.register(invalid);
        }).isInstanceOf(ConstraintViolationException.class);
    }
}