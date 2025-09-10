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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ui.Model;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class UserControllerTest {

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



    private UserController userController;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(securityMock, userServiceMock);
    }

    @Test
    public void userSkills_user1_is_null() {

        when(userServiceMock.getUserById(Matchers.any())).thenReturn(null);

        String response = userController.userSkills(httpServletRequestMock, httpServletResponseMock, UUID.randomUUID().toString());

        assertEquals("error", response);
    }

    @Test
    public void userSkills1_cookie_is_null() {

        when(userServiceMock.getUserById(Matchers.any())).thenReturn(new User());

        when(securityMock.validateSession(httpServletRequestMock)).thenReturn(null);
        String response = userController.userSkills(httpServletRequestMock, httpServletResponseMock, UUID.randomUUID().toString());

        assertEquals("redirect:/login", response);
    }

    @Test
    public void userSkills1_is_not_null() {
        when(userServiceMock.getUserById(Matchers.any())).thenReturn(new User());

        when(securityMock.validateSession(httpServletRequestMock)).thenReturn(new Cookie("1","2"));
        String response = userController.userSkills(httpServletRequestMock, httpServletResponseMock, UUID.randomUUID().toString());

        assertEquals("addskills", response);
    }

    @Test
    public void userSkills_user2_is_null() {
        when(userServiceMock.getUserById(Matchers.any())).thenReturn(null);

        String response = userController.userSkills(null, UUID.randomUUID().toString());

        assertEquals("error", response);
    }

    @Test
    public void userSkills_user2_is_not_null() {
        when(userServiceMock.getUserById(Matchers.any())).thenReturn(new User());

        String response = userController.userSkills(null, UUID.randomUUID().toString());

        assertEquals("redirect:/", response);
    }

    @Test
    public void newPersonalInfoGet_user_is_null() {
        when(userServiceMock.getUserById(Matchers.any())).thenReturn(null);
        String response = userController.newPersonalInfoGet(httpServletRequestMock, httpServletResponseMock, UUID.randomUUID().toString());
        assertEquals("error", response);
    }

    @Test
    public void newPersonalInfoGet_user_is_not_null() {
        when(userServiceMock.getUserById(Matchers.any())).thenReturn(new User());
        String response = userController.newPersonalInfoGet(httpServletRequestMock, httpServletResponseMock, UUID.randomUUID().toString());
        assertEquals("newPersonalInfo", response);
    }

    @Test
    public void newPersonalInfoPost_is_null() {
        when(userServiceMock.getUserById(Matchers.any())).thenReturn(null);
        String response = userController.newPersonalInfoPost(UUID.randomUUID().toString(), userMock, modelMock);
        assertEquals("error", response);
    }

    @Test
    public void newPersonalInfoPost_is_not_null() {
        when(userServiceMock.getUserById(Matchers.any())).thenReturn(new User());
        String response = userController.newPersonalInfoPost(UUID.randomUUID().toString(), userMock, modelMock);
        assertEquals("redirect:/users/"+ userMock.getId() + "/skills", response);
    }

}
