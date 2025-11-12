package com.example.GestoreAgenti.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class JwtUtilTest {

    @Test
    void generateTokenSupportsShortSecretByStrengtheningIt() throws Exception {
        JwtUtil util = new JwtUtil();
        setField(util, "secret", "supersegreto123456789");
        setField(util, "expirationMillis", 60000L);

        String token = util.generateToken("utente.demo");

        assertThat(token).isNotBlank();
        assertThat(util.extractUsername(token)).isEqualTo("utente.demo");
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JwtUtil.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
