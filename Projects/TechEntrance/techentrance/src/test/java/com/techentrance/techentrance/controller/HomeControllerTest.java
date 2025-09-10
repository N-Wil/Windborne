package com.techentrance.techentrance.controller;


import com.techentrance.techentrance.security.Security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HomeControllerTest {

    @Mock
    private Security securityMock;

    @Mock
    private HttpServletResponse httpServletResponseMock;

    private HomeController homeController;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        homeController = new HomeController(securityMock);
    }

    @Test
    public void home_cookie_not_null() {
        // Arrange
        when(securityMock.validateSession(Matchers.any())).thenReturn(new Cookie("test", "testValue"));

        // ACT
        String response = homeController.home(null, httpServletResponseMock);

        // ASSERT
        assertEquals("index", response);

    }

    @Test
    public void home_cookie_is_null() {
        // Arrange
        when(securityMock.validateSession(Matchers.any())).thenReturn(null);

        // ACT
        String response = homeController.home(null, httpServletResponseMock);

        // ASSERT
        assertEquals("redirect:/login", response);

    }
}
