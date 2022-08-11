package com.dev.springboot.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderTest {
    public static void main(String [] args){
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        String encodedPWD = bpe.encode("devs@A!5003");
        System.out.println(encodedPWD);
    }
}
