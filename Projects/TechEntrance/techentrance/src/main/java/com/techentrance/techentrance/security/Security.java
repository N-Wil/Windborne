package com.techentrance.techentrance.security;

import com.techentrance.techentrance.model.User;
import com.techentrance.techentrance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Security {
    private final UserService userService;

    public UUID authenticate(User user) {
        User foundUser = userService.getUserByEmail(user.getEmail());
        if(foundUser==null) {
            return null;
        }
        if(foundUser.getPassword().equals(user.getPassword())) {
            UUID sessionId = UUID.randomUUID();
            foundUser.setSessionId(sessionId);
            userService.saveUser(foundUser);
            return sessionId;
        }
        return null;
    }

    public Cookie validateSession(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            for (Cookie cookie : cookies){
                if(cookie.getName().equals("SessionId")){
                    // validate sessionID
                    User foundUser = userService.getUserBySessionId(UUID.fromString(cookie.getValue()));

                    if(foundUser != null){
                        cookie.setMaxAge(10);
                        return cookie;
                    }

                }
            }
        }
        return null;
    }

    public boolean validSession(UUID sessionId) {
        User foundUser = userService.getUserBySessionId(sessionId);
        if(foundUser==null) {
            return false;
        }
        return true;
    }
}