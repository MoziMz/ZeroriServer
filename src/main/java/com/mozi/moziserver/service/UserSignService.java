package com.mozi.moziserver.service;

import com.mozi.moziserver.model.UserAccount;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignService implements UserDetailsService {
    private final UserAuthRepository userAuthRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth userAuth = userAuthRepository.findUserAuthByTypeAndId(UserAuthType.ID, username)
                .orElse(null);

        if(userAuth == null) {
            return null;
        }

        return new UserAccount(userAuth);
    }
}
