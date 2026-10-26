package com.notes.selling.service.implementation;

import com.notes.selling.dto.UserDetailsDto;
import com.notes.selling.helper.Provider;
import com.notes.selling.repo.AuthRepository;
import com.notes.selling.service.blueprint.AuthService;
import com.notes.selling.service.blueprint.CustomOAuth2UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserServiceImple extends DefaultOAuth2UserService implements CustomOAuth2UserService  {

    private final AuthRepository authRepository ;
    private final AuthService authService ;

    private String subId ;
    private String email ;
    private String name ;
    private String provider ;

    @PostConstruct
    public void init() {
        System.out.println("CustomOAuth2UserServiceImple bean created");
    }

    public OAuth2User loadUser(OAuth2UserRequest request){
        System.out.println("Entered load user .");
        OAuth2User oAuth2User = super.loadUser(request);
        provider = request.getClientRegistration().getRegistrationId();
        if(provider.equals("google")) subId = oAuth2User.getAttribute("sub");
        if(provider.equals("github")) subId = oAuth2User.getAttribute("id");
        name = oAuth2User.getAttribute("name");
        email = oAuth2User.getAttribute("email");
        setParameter();
        return oAuth2User;
    }

    public void setParameter(){
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setProviderId(subId);
        userDetailsDto.setUsername(name);
        userDetailsDto.setEmail(email);
        userDetailsDto.setProvider(Provider.valueOf(provider.toUpperCase()));
        System.out.println(userDetailsDto);
        authService.register(userDetailsDto);
    }

}
