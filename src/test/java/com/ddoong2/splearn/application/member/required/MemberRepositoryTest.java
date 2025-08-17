package com.ddoong2.splearn.application.member.required;

import com.ddoong2.splearn.domain.member.Member;
import com.ddoong2.splearn.domain.member.MemberFixture;
import com.ddoong2.splearn.domain.member.MemberStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static com.ddoong2.splearn.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("멤버생성")
    void _멤버생성() {
        Member member = Member.register(MemberFixture.createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        Member found = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    @DisplayName("중복 이메일 회원 저장 실패")
    void _중복_이메일_회원_저장_실패() {
        Member member = Member.register(MemberFixture.createMemberRegisterRequest(), createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(MemberFixture.createMemberRegisterRequest(), createPasswordEncoder());
        assertThatThrownBy(() -> {
            memberRepository.save(member2);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}