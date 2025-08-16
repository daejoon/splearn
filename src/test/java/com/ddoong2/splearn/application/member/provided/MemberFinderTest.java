package com.ddoong2.splearn.application.member.provided;

import com.ddoong2.splearn.SplearnTestConfiguration;
import com.ddoong2.splearn.domain.member.Member;
import com.ddoong2.splearn.domain.member.MemberFixture;
import jakarta.persistence.EntityManager;
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
record MemberFinderTest(
        MemberFinder memberFinder,
        MemberRegister memberRegister,
        EntityManager entityManager
) {

    @Test
    @DisplayName("find")
    void _find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(member.getId()).isEqualTo(found.getId());
    }

    @Test
    @DisplayName("findFail")
    void _findFail() {
        Assertions.assertThatThrownBy(() -> {
            memberFinder.find(999L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}