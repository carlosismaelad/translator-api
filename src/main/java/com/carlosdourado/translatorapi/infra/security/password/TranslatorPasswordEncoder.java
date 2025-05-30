package com.carlosdourado.translatorapi.infra.security.password;

import com.carlosdourado.translatorapi.application.exceptions.TranslatorPasswordEncoderException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TranslatorPasswordEncoder {
    public static String encode(String password, String salt){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodeHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte b : encodeHash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException ex){
            throw new TranslatorPasswordEncoderException("Erro ao criptografar senha: " + ex.getMessage());

        }
    }
}
