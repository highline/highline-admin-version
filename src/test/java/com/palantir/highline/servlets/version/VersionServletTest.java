package com.palantir.highline.servlets.version;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

import static org.hamcrest.Matchers.equalTo;
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

    private StringWriter stringWriter = new StringWriter();
    private PrintWriter printWriter = new PrintWriter(stringWriter);

    @Before
    public void before() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getMethod()).thenReturn("GET");
    }

    private void assertVersionsMatch(Servlet servlet, String version) throws ServletException, IOException {
        servlet.service(request, response);
        String expectedVersion = version + "\n";
        String actualVersion = stringWriter.toString();

        assertThat(actualVersion, equalTo(expectedVersion));
    }

    private void setVersionTo(String version) {
        PowerMockito.mockStatic(VersionDetector.class);
        when(VersionDetector.detectVersion()).thenReturn(version);
    }

    @Test
    public void takesFixedVersion() throws ServletException, IOException {
        String version = "1.0.0";
        Servlet servlet = VersionServlet.withFixedVersion(version);

        assertVersionsMatch(servlet, version);
    }

    @Test
    public void takesAnotherFixedVersion() throws ServletException, IOException {
        String version = "1.2.3";
        Servlet servlet = VersionServlet.withFixedVersion(version);

        assertVersionsMatch(servlet, version);
    }

    @Test(expected = NullPointerException.class)
    public void fixedVersionCannotBeNull() {
        VersionServlet.withFixedVersion(null);
    }

    @Test
    public void checkHttpMetaData() throws ServletException, IOException {
        Servlet servlet = VersionServlet.withFixedVersion("1.0.0");

        servlet.service(request, response);

        verify(response).setStatus(eq(VersionServlet.RESPONSE_STATUS));
        verify(response).setContentType(eq(VersionServlet.RESPONSE_CONTENT_TYPE));
        verify(response).addHeader(eq(VersionServlet.RESPONSE_HEADER_CACHE_CONTROL), eq(VersionServlet.RESPONSE_CACHE_CONTROL));
    }

    @Test
    public void canDetectVersion() throws IOException, ServletException {
        String version = "1.0.0";
        setVersionTo(version);
        Servlet servlet = VersionServlet.detectVersion(version);

        assertVersionsMatch(servlet, version);
    }

    @Test
    public void canDetectAnotherVersion() throws IOException, ServletException {
        String version = "1.2.3";
        setVersionTo(version);
        Servlet servlet = VersionServlet.detectVersion(version);

        assertVersionsMatch(servlet, version);
    }

    @Test
    public void usesDynamicVersionWhenCantDerive() throws IOException, ServletException {
        setVersionTo(null);
        String version = "1.0.0";
        Servlet servlet = VersionServlet.detectVersion(version);

        servlet.service(request, response);

        String actualVersion = stringWriter.toString();
        String expectedVersion = version + VersionServlet.DEFAULT_MIDFIX;
        assertThat(actualVersion, startsWith(expectedVersion));
    }

    @Test(expected = NullPointerException.class)
    public void detectVersionDefaultCannotBeNull() {
        VersionServlet.detectVersion(null);
    }
}
