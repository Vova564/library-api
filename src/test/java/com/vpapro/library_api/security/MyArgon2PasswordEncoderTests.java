package com.vpapro.library_api.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MyArgon2PasswordEncoderTests {

    private static final MyArgon2PasswordEncoder passwordEncoder = new MyArgon2PasswordEncoder();
    private static String storedPassword;

    @BeforeAll
    static void init() {
        storedPassword = passwordEncoder.encode("password");
    }

    @Test
    void matches_whenCorrectPasswordWasChecked_returnTrue() {
        String password = "password";

        assertThat(passwordEncoder.matches(password, storedPassword)).isTrue();
    }

    @Test
    void matches_whenIncorrectPasswordWasChecked_returnFalse() {
        String password = "wrong_password";

        assertThat(passwordEncoder.matches(password, storedPassword)).isFalse();
    }

    @Test
    void encode_whenTwoSamePasswordsProvided_returnDifferentHashes() {
        String password = "password";

        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        assertThat(hash1).isNotEqualTo(hash2);
    }
}
