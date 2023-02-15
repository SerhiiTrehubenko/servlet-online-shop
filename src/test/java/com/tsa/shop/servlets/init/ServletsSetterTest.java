package com.tsa.shop.servlets.init;

import jakarta.servlet.http.HttpServlet;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServletsSetterTest {

    @Test
    void testGetHttpServletClasses() {
        String expectedClass = "class com.tsa.shop.servlets.ContentFilesServlet";
        List<Class<HttpServlet>> classList = ServletsSetter.getHttpServletClasses();
        assertNotNull(classList);
        assertTrue(classList.toString().contains(expectedClass));
    }

    @Test
    void testGetInstancesOfHttpClasses() {
        String expectedInstance = "com.tsa.shop.servlets.ContentFilesServlet";
        List<Class<HttpServlet>> classList = ServletsSetter.getHttpServletClasses();
        List<HttpServlet> httpServletList = ServletsSetter.getInstancesOfHttpClasses(classList);
        assertNotNull(httpServletList);

        assertTrue(httpServletList.toString().contains(expectedInstance));
    }

    @Test
    void testGetUriFromInstanceOfHttpServlet() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String expected = "/home";
        List<Class<HttpServlet>> classList = ServletsSetter.getHttpServletClasses();
        List<HttpServlet> httpServletList = ServletsSetter.getInstancesOfHttpClasses(classList);
        assertNotNull(httpServletList);

        Method method = httpServletList.get(0).getClass().getDeclaredMethod("getUri");
        Object uri = method.invoke(httpServletList.get(0));
        String uriAsString = String.valueOf(uri);

        assertEquals(expected, uriAsString);

    }
}