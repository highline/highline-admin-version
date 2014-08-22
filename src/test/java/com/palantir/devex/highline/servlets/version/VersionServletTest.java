package com.palantir.devex.highline.servlets.version;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VersionDetector.class)
public class VersionServletTest {

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintWriter printWriter = new PrintWriter(baos);

    @Before
    public void before() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getMethod()).thenReturn("GET");
    }

    private void assertVersionMatches(Servlet servlet, String version) throws ServletException, IOException {
        servlet.service(request, response);
        assertThat(baos.toString(), startsWith(version));
    }

    private void setVersionTo(String version) {
        PowerMockito.mockStatic(VersionDetector.class);
        when(VersionDetector.detectVersion()).thenReturn(version);
    }

    @Test
    public void takesHardcodedVersion() throws ServletException, IOException {
        String version = "1.0.0";
        Servlet servlet = VersionServlet.withVersion(version);

        assertVersionMatches(servlet, version);
    }

    @Test
    public void takesAnotherHardcodedVersion() throws ServletException, IOException {
        String version = "1.2.3";
        Servlet servlet = VersionServlet.withVersion(version);

        assertVersionMatches(servlet, version);
    }

    @Test(expected = NullPointerException.class)
    public void explicitVersionCannotBeNull() {
        VersionServlet.withVersion(null);
    }

    @Test
    public void checkHttpMetaData() throws ServletException, IOException {
        Servlet servlet = VersionServlet.withVersion("1.0.0");

        servlet.service(request, response);

        verify(response).setStatus(eq(200));
        verify(response).setContentType(eq("text/plain"));
        verify(response).addHeader(eq("Cache-Control"), eq("must-revalidate,no-cache,no-store"));
    }

    @Test
    public void canDeriveVersion() throws IOException, ServletException {
        String version = "1.0.0";
        setVersionTo(version);
        Servlet servlet = VersionServlet.deriveVersion();

        assertVersionMatches(servlet, version);
    }

    @Test
    public void canDeriveAnotherVersion() throws IOException, ServletException {
        String version = "1.2.3";
        setVersionTo(version);
        Servlet servlet = VersionServlet.deriveVersion();

        assertVersionMatches(servlet, version);
    }
}
