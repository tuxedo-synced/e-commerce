package com.notes.selling.security.service.blueprint;

import com.notes.selling.dto.UserDetailsDto;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CustomOAuth2UserService extends OAuth2UserService<OAuth2UserRequest , OAuth2User> {
    public void setParameter();
    public void register(UserDetailsDto userDetailsDto);
}
