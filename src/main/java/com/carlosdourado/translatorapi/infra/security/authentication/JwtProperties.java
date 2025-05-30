package com.carlosdourado.translatorapi.infra.security.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private String secretKey;
    private long expirationTime;

}
