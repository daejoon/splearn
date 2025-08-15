package com.ddoong2.splearn.application.provided;

import com.ddoong2.splearn.SplearnTestConfiguration;
import com.ddoong2.splearn.domain.DuplicateEmailException;
import com.ddoong2.splearn.domain.Member;
import com.ddoong2.splearn.domain.MemberFixture;
import com.ddoong2.splearn.domain.MemberStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister) {

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
        Assertions.assertThatThrownBy(() -> {
            memberRegister.register(MemberFixture.createMemberRegisterRequest());
        }).isInstanceOf(DuplicateEmailException.class);
    }

}