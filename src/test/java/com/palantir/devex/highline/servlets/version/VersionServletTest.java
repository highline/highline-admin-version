package com.palantir.devex.highline.servlets.version;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class VersionServletTest {

    @Test
    public void takesHardcodedVersion() throws ServletException, IOException {
        String version = "1.0.0";
        Servlet servlet = VersionServlet.withVersion(version);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(baos);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getMethod()).thenReturn("GET");
        servlet.service(request, response);

        assertThat(baos.toString(), startsWith(version));
    }

    @Test(expected = NullPointerException.class)
    public void explicitVersionCannotBeNull() {
        VersionServlet.withVersion(null);
    }

    @Test
    public void checkHttpMetaData() throws ServletException, IOException {
        Servlet servlet = VersionServlet.withVersion("1.0.0");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(baos);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getMethod()).thenReturn("GET");
        servlet.service(request, response);

        verify(response).setStatus(eq(200));
        verify(response).setContentType(eq("text/plain"));
        verify(response).addHeader(eq("Cache-Control"), eq("must-revalidate,no-cache,no-store"));
    }
}
