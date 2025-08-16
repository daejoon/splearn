package com.ddoong2.splearn.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    @DisplayName("profile")
    void _profile() {
        new Profile("daejoon");
        new Profile("daejoon100");
        new Profile("1234");
    }

    @Test
    @DisplayName("profileFail")
    void _profileFail() {
        assertThatThrownBy(() -> new Profile("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("asdfkajsdfkjaskdfjkasjdfkajskdjfaksjfkajkfjaskfd")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("AAA")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("url")
    void _url() {
        Profile profile = new Profile("daejoon");

        assertThat(profile.url()).isEqualTo("@daejoon");
    }

}