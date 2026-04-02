package com.vpapro.library_api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JwtUtilsTests {

    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "test_secret_fjdsajfoipwd5nm5fknmcklvmxcbp23ojkeopjqjakjsdlfjasllcz");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 360000);
        jwtUtils.init();
    }

    @Test
    void getUserFromToken_whenTokenIsValid_returnsEmail() {
        String token = jwtUtils.generateToken("test@test.com");

        String result = jwtUtils.getUserFromToken(token);

        assertThat(result).isEqualTo("test@test.com");
    }

    @Test
    void validateJwtToken_whenTokenIsValid_returnsTrue() {
        String token = jwtUtils.generateToken("test@test.com");

        boolean result = jwtUtils.validateJwtToken(token);

        assertThat(result).isTrue();
    }

    @Test
    void validateJwtToken_whenTokenExpired_returnsFalse() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 10); // 10ms
        jwtUtils.init();
        String token = jwtUtils.generateToken("test@test.com");

        Thread.sleep(20);

        boolean result = jwtUtils.validateJwtToken(token);
        assertThat(result).isFalse();
    }

    @Test
    void validateJwtToken_whenTokenWithWrongSignature_returnsFalse() {
        String token = jwtUtils.generateToken("test@test.com");

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "wrong_secret_fjdsajfoipwd5nm5fknmcklvmxcbp23ojkeopjqjakjsdlfjasllcz");
        jwtUtils.init();

        boolean result = jwtUtils.validateJwtToken(token);
        assertThat(result).isFalse();
    }
}
