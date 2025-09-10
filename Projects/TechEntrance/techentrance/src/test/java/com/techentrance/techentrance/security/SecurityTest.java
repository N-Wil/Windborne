package com.techentrance.techentrance.security;

import com.techentrance.techentrance.Utils;
import com.techentrance.techentrance.controller.LoginController;
import com.techentrance.techentrance.model.User;
import com.techentrance.techentrance.security.Security;
import com.techentrance.techentrance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class SecurityTest {

    @Mock
    private UserService userServiceMock;

    @Mock
    private Security securityMock;

    @Mock
    private HttpServletResponse httpServletResponseMock;

    @Mock
    private Utils utilsMock;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private User userMock1;

    @Mock
    private User userMock2;

    @Mock
    private Model modelMock;

    private Security security;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        security = new Security(userServiceMock);
    }

    @Test
    public void authenticate_user_is_null() {
        when(userServiceMock.getUserByEmail(Matchers.any())).thenReturn(null);
        Object response = security.authenticate(userMock1);

        assertNull(response);
    }

    @Test
    public void authenticate_password_found() {
        //User user = new User();
        when(userServiceMock.getUserByEmail(Matchers.any())).thenReturn(userMock1);
        when(userMock1.getPassword()).thenReturn("1");
        when(userMock2.getPassword()).thenReturn("1");

        Object response = security.authenticate(userMock2).toString();

        assertNotNull(response);
    }

    @Test
    public void authenticate_password_not_found() {
        when(userServiceMock.getUserByEmail(Matchers.any())).thenReturn(userMock2);
        when(userMock1.getPassword()).thenReturn("2");
        when(userMock2.getPassword()).thenReturn("1");

        Object response = security.authenticate(userMock1);

        assertNull(response);
    }

    @Test
    public void validateSession1_is_null() {
        when(httpServletRequestMock.getCookies()).thenReturn(null);
        Object response = security.validateSession(httpServletRequestMock);
        assertNull(response);
    }

    @Test
    public void validateSession1_is_not_null() {
        Cookie cookie1 = new Cookie("Session", UUID.randomUUID().toString());
        Cookie cookie2 = new Cookie("SessionId", UUID.randomUUID().toString());
        Cookie[] arr = new Cookie[] {cookie1, cookie2};
        when(httpServletRequestMock.getCookies()).thenReturn(arr);
        when(userServiceMock.getUserBySessionId(Matchers.any())).thenReturn(new User());
        Object response = security.validateSession(httpServletRequestMock);
        assertNotNull(response);
    }

    @Test
    public void validSession_not_null() {
        when(userServiceMock.getUserBySessionId(Matchers.any())).thenReturn(new User());
        boolean response = security.validSession(UUID.randomUUID());
        assertTrue(response);
    }

    @Test
    public void validSession_null() {
        when(userServiceMock.getUserBySessionId(Matchers.any())).thenReturn(null);
        boolean response = security.validSession(UUID.randomUUID());
        assertFalse(response);
    }
}
