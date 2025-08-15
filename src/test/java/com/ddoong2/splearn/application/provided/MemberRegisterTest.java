package com.ddoong2.splearn.application.provided;

import com.ddoong2.splearn.application.MemberService;
import com.ddoong2.splearn.application.required.EmailSender;
import com.ddoong2.splearn.application.required.MemberRepository;
import com.ddoong2.splearn.domain.Email;
import com.ddoong2.splearn.domain.Member;
import com.ddoong2.splearn.domain.MemberFixture;
import com.ddoong2.splearn.domain.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MemberRegisterTest {

    @Test
    @DisplayName("스텁을 이용한 멤버등록 확인")
    void _멤버등록() {
        MemberRegister register = new MemberService(
                new MemberRepositoryStub(),
                new EmailSenderStub(),
                MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("목을 이용한 멤버등록 확인")
    void _목을_이용한_멤버등록_확인() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
        MemberRegister register = new MemberService(
                new MemberRepositoryStub(),
                emailSenderMock,
                MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {
        }
    }

    static class EmailSenderMock implements EmailSender {
        List<Email> tos = new ArrayList<>();

        @Override
        public void send(Email email, String subject, String body) {
            this.tos.add(email);
        }

        public List<Email> getTos() {
            return this.tos;
        }
    }
}