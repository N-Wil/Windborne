package com.techentrance.techentrance;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            for (Cookie c : cookies){
                if(c.getName().equals(cookieName)){
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
