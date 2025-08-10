package com.ddoong2.splearn.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @Test
    void createMember() {
        var member = new Member("kkode1911@gmail.com", "daejoon", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("생성자에서 널 체크를 잘 하는가")
    void _생성자에서_널_체크를_잘_하는가() {

        assertThatThrownBy(() -> new Member(null, "daejoon", "secret"))
                .isInstanceOf(NullPointerException.class);
    }


    @Test
    @DisplayName("멤버를 활성화 한다")
    void _멤버를_활성화_한다() {

        var member = new Member("kkode1911@gmail", "daejoon", "secret");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }

    @Test
    @DisplayName("멤버 활성화 실패")
    void _멤버_활성화_실패() {
        var member = new Member("kkode1911@gmail", "daejoon", "secret");

        member.activate();

        assertThatThrownBy(() -> {
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("가입완료 상태에서 탈퇴할수 있다")
    void _가입완료_상태에서_탈퇴할수_있다() {

        var member = new Member("kkode1911@gmail", "daejoon", "secret");
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATE);
    }

    @Test
    void deactivateFail() {
        var member = new Member("kkode1911@gmail", "daejoon", "secret");

        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();
        
        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }
}