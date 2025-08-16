package com.ddoong2.splearn.application.member.provided;

import com.ddoong2.splearn.SplearnTestConfiguration;
import com.ddoong2.splearn.domain.member.DuplicateEmailException;
import com.ddoong2.splearn.domain.member.DuplicateProfileException;
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
        Member member = registerMember();

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("이메일 중복 실패")
    void _이메일_중복_실패() {
        registerMember();
        assertThatThrownBy(() -> {
            registerMember();
        }).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("activated")
    void _activated() {
        Member member = registerMember();
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
        Member member = registerMember();
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
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("daejoon", "dj", "자기소개"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("dj");
    }

    @Test
    @DisplayName("updateInfoFail")
    void _updateInfoFail() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("daejoon", "dj", "자기소개"));

        Member member2 = registerMember("kkode200@gmail.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        // member2는 기존의 member와 같은 profile을 사용할수 없다.
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("daejoon1", "dj", "Introduction"));
        }).isInstanceOf(DuplicateProfileException.class);

        // 다른 프로필 주소로는 변경 가능
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("daejoon1", "dj1", "Introduction"));

        // 기존 프로필 주소는 빠꾸는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("daejoon", "dj", "Introduction"));

        // 프로필 주소를 제거하는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("daejoon", "", "Introduction"));

        // 프로필 주소 중복은 허용하지 않음
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("daejoon", "dj1", "Introduction"));
        }).isInstanceOf(DuplicateProfileException.class);
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

    private Member registerMember() {
        Member register = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return register;
    }

    private Member registerMember(String email) {
        Member register = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return register;
    }
}