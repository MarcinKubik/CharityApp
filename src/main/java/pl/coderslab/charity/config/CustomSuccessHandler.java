package pl.coderslab.charity.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException{

        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for(GrantedAuthority grantedAuthority : authorities){
            if(grantedAuthority.getAuthority().equals("ROLE_USER")){
                redirectUrl = "/users/user/userPage";
                break;
            }else if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                redirectUrl = "/admins/sb-admin-2";
            }
        }

        if(redirectUrl == null){
            redirectUrl = "/403";
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
