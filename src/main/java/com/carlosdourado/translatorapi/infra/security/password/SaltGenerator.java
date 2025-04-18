package com.carlosdourado.translatorapi.infra.security.password;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltGenerator {
    public static String saltGenerator(){
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
