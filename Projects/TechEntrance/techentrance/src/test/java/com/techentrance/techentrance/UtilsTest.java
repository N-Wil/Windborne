package com.techentrance.techentrance;

import com.techentrance.techentrance.model.User;
import com.techentrance.techentrance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.when;


public class UtilsTest {

    @Mock
    private UserService userServiceMock;

    @Mock
    private HttpServletResponse httpServletResponseMock;

    @Mock
    private Utils utilsMock;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private User userMock1;

    @Mock
    private Model modelMock;

    //private Utils util;
    private String cookieStringMock;


    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void authenticate_request_null() {
        when(httpServletRequestMock.getCookies()).thenReturn(null);
        Object response = Utils.getCookie(httpServletRequestMock, cookieStringMock);

        assertNull(response);
    }

    @Test
    public void authenticate_request_not_null() {
        Cookie[] cookies = new Cookie[2];
        cookies[0] = new Cookie("name1", "value1");
        cookies[1] = new Cookie("name2", "value2");

        when(httpServletRequestMock.getCookies()).thenReturn(cookies);


        Object response = Utils.getCookie(httpServletRequestMock, cookies[0].getName());

        assertNotNull(response);
    }

    /*
    @Test
    public void authenticate_null_cookie_name() {
        when(httpServeletRequestMock.getCookie(Matcher.any())).thenReturn(cookieArrayMock);

        Object responce = Utils.getCookie(httpServletRequestMock, null);

        assertNull(responce);


    }
    */

    
}