package com.chouchase.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class MD5Utils {
    private static final String salt = "d582jjdyx3";
    public static String encrypt(String str){
        return DigestUtils.md5DigestAsHex((str +salt).getBytes(StandardCharsets.UTF_8));
    }
}
