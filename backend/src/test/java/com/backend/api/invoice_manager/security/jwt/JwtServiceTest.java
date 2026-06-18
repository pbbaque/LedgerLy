package com.backend.api.invoice_manager.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {

    private static final String TEST_SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", TEST_SECRET);
    }

    @Test
    void generatesTokenWithSubjectAndCustomClaims() {
        String token = jwtService.generateToken(
            Map.of("authorities", List.of("ROLE_COMPANY_ADMIN"), "companyId", 1),
            "demo@ledgerly.local"
        );

        assertEquals("demo@ledgerly.local", jwtService.extractUsername(token));
        Number companyId = jwtService.extractClaim(token, claims -> claims.get("companyId", Number.class));
        assertEquals(1L, companyId.longValue());
        assertEquals(List.of("ROLE_COMPANY_ADMIN"), jwtService.extractClaim(token, claims -> claims.get("authorities", List.class)));
    }

    @Test
    void validatesTokenAgainstMatchingUserDetails() {
        String token = jwtService.generateToken(Map.of(), "demo@ledgerly.local");
        UserDetails userDetails = User.withUsername("demo@ledgerly.local")
            .password("encoded-password")
            .roles("COMPANY_ADMIN")
            .build();

        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void rejectsTokenForDifferentUserDetails() {
        String token = jwtService.generateToken(Map.of(), "demo@ledgerly.local");
        UserDetails anotherUser = User.withUsername("other@ledgerly.local")
            .password("encoded-password")
            .roles("USER")
            .build();

        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }
}
