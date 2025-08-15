package com.ddoong2.splearn.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    @DisplayName("동등성 비교")
    void _동등성_비교() {

        Email email1 = new Email("kkode1911@gmail.com");
        Email email2 = new Email("kkode1911@gmail.com");

        assertThat(email1).isEqualTo(email2);
    }

}