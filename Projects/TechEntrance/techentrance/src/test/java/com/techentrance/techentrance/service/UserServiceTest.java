package com.techentrance.techentrance.service;

import com.techentrance.techentrance.Utils;
import com.techentrance.techentrance.controller.LoginController;
import com.techentrance.techentrance.model.User;
import com.techentrance.techentrance.repo.UserRepository;
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

public class UserServiceTest {
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

    @Mock
    private UserRepository userRepositoryMock;

    private UserService userService;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepositoryMock);
    }

    @Test
    public void saveUser() {
        when(userRepositoryMock.save(Matchers.any())).thenReturn(new User());
        Object response = userService.saveUser(userMock1);
        assertNotNull(response);
    }

    @Test
    public void getUserByEmail() {
        when(userRepositoryMock.findByEmail(Matchers.any())).thenReturn(new User());
        Object response = userService.getUserByEmail("");
        assertNotNull(response);
    }

    @Test
    public void getUserBySessionId() {
        when(userRepositoryMock.findBySessionId(Matchers.any())).thenReturn(new User());
        Object response = userService.getUserBySessionId(UUID.randomUUID());
        assertNotNull(response);
    }

    @Test
    public void getUserById() {
        when(userRepositoryMock.findById(UUID.randomUUID())).thenReturn(new User());
        Object response = userService.getUserById(UUID.randomUUID());
        assertNull(response);
    }

}
