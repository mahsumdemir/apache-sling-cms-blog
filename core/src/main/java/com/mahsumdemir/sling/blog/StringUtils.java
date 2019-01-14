package com.mahsumdemir.sling.blog;

public class StringUtils {
    public static boolean isEmpty(String string) {
        if (string == null || string.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
