package com.notes.selling.security.service.implementation;

import com.notes.selling.dto.UserDetailsDto;
import com.notes.selling.entity.Users;
import com.notes.selling.helper.Provider;
import com.notes.selling.helper.Role;
import com.notes.selling.repo.AuthRepository;
import com.notes.selling.security.service.blueprint.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserServiceImple extends DefaultOAuth2UserService implements CustomOAuth2UserService  {

    private final AuthRepository authRepository ;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder ;

    private String subId ;
    private String email ;
    private String name ;
    private String provider ;

    public OAuth2User loadUser(OAuth2UserRequest request){
        System.out.println("Entered load user .");
        OAuth2User oAuth2User = super.loadUser(request);
        provider = request.getClientRegistration().getRegistrationId();
        if(provider.equals("google")) subId = oAuth2User.getAttribute("sub");
        if(provider.equals("github")){
            Object temp = oAuth2User.getAttribute("id");
            subId = String.valueOf(temp);
        }
        name = oAuth2User.getAttribute("name");
        email = oAuth2User.getAttribute("email");
        setParameter();
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_BUYER")),
                oAuth2User.getAttributes(),
                "google".equals(provider) ? "sub" : "id"
        );
    }

    public void setParameter() {

        Provider oauthProvider =
                Provider.valueOf(provider.toUpperCase());

        Optional<Users> existingUser =
                authRepository.findByProviderId(subId);

        if (existingUser.isPresent()) {
            System.out.println("User already exists: " + email);
            return;
        }

        UserDetailsDto dto = new UserDetailsDto();
        dto.setProviderId(subId);
        dto.setUsername(name);
        dto.setEmail(email);
        dto.setProvider(oauthProvider);
        dto.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        dto.setRole(Role.BUYER);

        register(dto);
    }

    public void register(UserDetailsDto dto) {

        Users user = modelMapper.map(dto, Users.class);

        authRepository.save(user);

        System.out.println("User saved successfully.");
    }

}
