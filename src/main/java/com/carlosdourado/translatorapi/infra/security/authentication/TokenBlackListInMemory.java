package com.carlosdourado.translatorapi.infra.security.authentication;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlackListInMemory {
    private final Set<String> blackList = ConcurrentHashMap.newKeySet();

    public void add(String token){
        blackList.add(token);
    }

    public boolean contains(String token){
        return blackList.contains(token);
    }
}
