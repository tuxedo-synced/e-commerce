package com.notes.selling.helper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = "/";

        // for my case its a single role , but we can also return multiple roles so below used stream(_ considering multiple roles .

        boolean admin = authentication.getAuthorities().stream().anyMatch(l -> l.getAuthority().equals("ROLE_ADMIN"));
        boolean buyer = authentication.getAuthorities().stream().anyMatch(l -> l.getAuthority().equals("ROLE_BUYER"));
        boolean seller = authentication.getAuthorities().stream().anyMatch(l -> l.getAuthority().equals("ROLE_SELLER"));

        if(admin) targetUrl = "/admin/interface";
        else if(buyer) targetUrl = "/buyer/interface";
        else if(seller) targetUrl = "/seller/interface";
        else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}
