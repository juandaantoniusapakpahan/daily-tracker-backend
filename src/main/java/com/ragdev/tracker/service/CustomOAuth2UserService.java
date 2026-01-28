package com.ragdev.tracker.service;

import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.enums.Role;
import com.ragdev.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private  UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String externalId;
        String email;
        String username;

        if ("google".equals(registrationId)) {
            externalId = String.valueOf(attributes.get("sub"));
            email = (String) attributes.get("email");
            username = (email != null) ? email.split("@")[0] : (String) attributes.get("name");
        } else {
            externalId = String.valueOf(attributes.get("id"));
            email = (String) attributes.get("email");
            username = (String) attributes.get("login");

            if (email == null) {
                email = username + "@github.com";
            }
        }

        String finalEmail = email;
        userRepository.findByExternalId(externalId).orElseGet(() -> {
            User newUser = new User();
            newUser.setExternalId(externalId);
            newUser.setEmail(finalEmail);
            newUser.setUsername(username);
            newUser.setRole(Role.USER);
            newUser.setPassword("");
            newUser.setIsActive(true);
            return userRepository.save(newUser);
        });

        return oAuth2User;
    }
}
