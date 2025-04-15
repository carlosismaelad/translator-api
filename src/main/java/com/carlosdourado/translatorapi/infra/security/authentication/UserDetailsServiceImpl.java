package com.carlosdourado.translatorapi.infra.security.authentication;

import com.carlosdourado.translatorapi.domain.repositories.TranslatorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TranslatorRepository repository;

    public UserDetailsServiceImpl(TranslatorRepository repository){
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        return (UserDetails) repository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado." + email));
    }

}
