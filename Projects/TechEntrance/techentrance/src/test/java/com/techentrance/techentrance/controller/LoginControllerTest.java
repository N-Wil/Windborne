package com.techentrance.techentrance.controller;

import com.techentrance.techentrance.Utils;
import com.techentrance.techentrance.model.User;
import com.techentrance.techentrance.security.Security;
import com.techentrance.techentrance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class LoginControllerTest {

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
    private User userMock;

    @Mock
    private Model modelMock;

    private LoginController loginController;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        loginController = new LoginController(userServiceMock, securityMock);
    }

    @Test
    public void loginView_is_null() {
        //assertEquals(null, Utils.getCookie(httpServletRequestMock, ""));
        when(securityMock.validateSession(Matchers.any())).thenReturn(null);
        //when(Utils.getCookie(Matchers.any(), Matchers.any())).thenReturn(null);
        String response = loginController.loginView(httpServletRequestMock, httpServletResponseMock);

        assertEquals("login", response);
    }

    @Test
    public void loginView_is_not_null() {
        when(securityMock.validateSession(Matchers.any())).thenReturn(new Cookie("test", "testValue"));
        String response = loginController.loginView(null, httpServletResponseMock);

        assertEquals("redirect:/", response);
    }

    @Test
    public void login_sessionID_is_null() {
        when(securityMock.authenticate(Matchers.any())).thenReturn(null);
        String response = loginController.login(httpServletRequestMock, httpServletResponseMock, userMock, modelMock);

        assertEquals("redirect:/login", response);
    }

    @Test
    public void login_redirect_is_null() {
        //assertEquals(null, Utils.getCookie(httpServletRequestMock, ""));
        when(securityMock.authenticate(Matchers.any())).thenReturn(new UUID(0,0));
        when(Utils.getCookie(httpServletRequestMock, "")).thenReturn(null);
        String response = loginController.login(httpServletRequestMock, httpServletResponseMock, userMock, modelMock);
        assertEquals("redirect:/", response);
    }





    @Test
    public void signUpView_not_null() {
        String response = loginController.signUpView();
        assertEquals("signup", response);
    }

    @Test
    public void userRegistration_not_null() {
        when(userServiceMock.getUserByEmail(Matchers.any())).thenReturn(new User());
        when(userMock.getId()).thenReturn(new UUID(1,1));

        String response = loginController.userRegistration(httpServletResponseMock, userMock, modelMock);
        assertEquals("signup", response);
    }

    @Test
    public void userRegistration_is_null() {
        when(userServiceMock.getUserByEmail(Matchers.any())).thenReturn(null);
        when(userMock.getId()).thenReturn(new UUID(1,1));

        String response = loginController.userRegistration(httpServletResponseMock, userMock, modelMock);
        assertEquals("redirect:/users/00000000-0000-0001-0000-000000000001/personalInfo", response);
    }
}
