package com.ddoong2.splearn.application.provided;

import com.ddoong2.splearn.SplearnTestConfiguration;
import com.ddoong2.splearn.domain.Member;
import com.ddoong2.splearn.domain.MemberFixture;
import com.ddoong2.splearn.domain.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
class MemberRegisterTest {
    @Autowired
    private MemberRegister memberRegister;

    @Test
    @DisplayName("스프링부트 등록테스트")
    void _등록테스트() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

}