package com.userwebapp;

import org.mindrot.jbcrypt.BCrypt;

public class HashGenerator {
    public static void main(String[] args) {
        String plain = "admin";
        String hashed = BCrypt.hashpw(plain, BCrypt.gensalt(10));
        System.out.println("BCrypt hash for \"" + plain + "\":");
        System.out.println(hashed);
    }
}
