package com.ragdev.tracker.config;

import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.repository.UserRepository;
import com.ragdev.tracker.security.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId();

        String externalId;
        if ("google".equals(registrationId)) {
            externalId = attributes.get("sub").toString();
        } else {
            externalId = attributes.get("id").toString();
        }

        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new RuntimeException("User not found after OAuth2 login"));

        String token = jwtService.generateToken(user.getUsername());

        String targetUrl = "http://localhost:5173/?token=" + token;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
