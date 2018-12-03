package com.app.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1,
                                        Authentication authentication) throws IOException, ServletException {

        boolean hasPatientRole = false;
        boolean hasDoctorRole = false;
        boolean hasAdminRole = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_PATIENT")) {
                hasPatientRole = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                hasAdminRole = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_DOCTOR")) {
                hasDoctorRole = true;
                break;
            }
        }

        if (hasPatientRole) {
            redirectStrategy.sendRedirect(arg0, arg1, "/patient/panel");
        } else if (hasAdminRole) {
            redirectStrategy.sendRedirect(arg0, arg1, "/admin/panel");
        } else if (hasDoctorRole) {
            redirectStrategy.sendRedirect(arg0, arg1, "/doctor/panel");
        } else {
            throw new IllegalStateException();
        }
    }
}
