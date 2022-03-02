package com.mozi.moziserver.security;

import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mozi.moziserver.common.Constant.EMAIL_DOMAIN_GROUPS;

@Service
@RequiredArgsConstructor
public class UserEmailSignInService implements UserDetailsService {
    private final UserAuthRepository userAuthRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username;

        int atIndex = email.lastIndexOf('@');
        String emailIdWithAt = email.substring(0, atIndex+1);
        String emailDomain = email.substring(atIndex+1).toLowerCase();
        List<String> currentDomainGroup = null;

        for(List<String> domainGroup : EMAIL_DOMAIN_GROUPS) {
            if(domainGroup.contains(emailDomain)) {
                currentDomainGroup = domainGroup;
                break;
            }
        }

        if(currentDomainGroup == null) {
            return null;
        }

        for(String domain : currentDomainGroup) {
            Optional<UserAuth> userAuthOptional = userAuthRepository.findUserAuthByTypeAndId(UserAuthType.EMAIL, emailIdWithAt + domain);
            if(userAuthOptional.isPresent()) {
                return new ResUserSignIn(userAuthOptional.get());
            }
        }

        return null;
    }
}
