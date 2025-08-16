package com.ddoong2.splearn.application.member.provided;

import com.ddoong2.splearn.SplearnTestConfiguration;
import com.ddoong2.splearn.domain.member.DuplicateEmailException;
import com.ddoong2.splearn.domain.member.Member;
import com.ddoong2.splearn.domain.member.MemberFixture;
import com.ddoong2.splearn.domain.member.MemberInfoUpdateRequest;
import com.ddoong2.splearn.domain.member.MemberRegisterRequest;
import com.ddoong2.splearn.domain.member.MemberStatus;
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
        Member member = registerNewMember();

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("이메일 중복 실패")
    void _이메일_중복_실패() {
        registerNewMember();
        assertThatThrownBy(() -> {
            registerNewMember();
        }).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("activated")
    void _activated() {
        Member member = registerNewMember();
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    @DisplayName("deactivated")
    void _deactivated() {
        Member member = registerNewMember();
        entityManager.flush();
        entityManager.clear();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATE);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    @DisplayName("updateInfo")
    void _updateInfo() {
        Member member = registerNewMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("daejoon", "dj", "자기소개"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("dj");
    }

    @Test
    @DisplayName("memberRegisterRequestFail")
    void _memberRegisterRequestFail() {
        checkValidation(new MemberRegisterRequest("fail@gmail.com", "1234", "long_secret"));
        checkValidation(new MemberRegisterRequest("fail@gmail.com", "daejoon_________", "secret"));
        checkValidation(new MemberRegisterRequest("failgmail.com", "daejoon_________", "long_secret"));
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> {
            memberRegister.register(invalid);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    private Member registerNewMember() {
        return memberRegister.register(MemberFixture.createMemberRegisterRequest());
    }
}