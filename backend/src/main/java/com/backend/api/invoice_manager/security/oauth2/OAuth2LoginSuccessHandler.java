package com.backend.api.invoice_manager.security.oauth2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.responses.AuthTokenResponse;
import com.backend.api.invoice_manager.security.jwt.JwtService;
import com.backend.api.invoice_manager.services.user.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        User authUser = userService.findByEmail(authentication.getName());
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authentication.getAuthorities() 
            .stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority())
            .toList());
        String token = jwtService.generateToken(claims ,user.getUsername());
        AuthTokenResponse.writeTokenSuccessfulResponse(response, token, authUser);
    }
}
