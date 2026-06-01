package com.backend.api.invoice_manager.responses;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenResponse {

    private static final String PREFIX_TOKEN = "Bearer ";
    private static final String HEADER_AUTH = "Authorization";
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARSET = "UTF-8";

    public static void writeTokenSuccessfulResponse(HttpServletResponse response, String token, User user) throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("data", user);
        body.put("message", String.format("Login successful %s", user.getUsername()));
        body.put("statusCode", HttpStatus.OK.value());
        body.put("success", true);

        response.addHeader(HEADER_AUTH, PREFIX_TOKEN + token);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARSET);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.getWriter().flush();
    }

    public static void writeTokenUnsuccessfulResponse(HttpServletResponse response, AuthenticationException failed) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Login failed!! Email or password is incorrect!!");
        body.put("error", "Bad credentials");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.getWriter().flush();
    }
}
