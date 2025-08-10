package com.ddoong2.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    void createMember() {
        var member = new Member("kkode1911@gmail.com", "daejoon", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

}