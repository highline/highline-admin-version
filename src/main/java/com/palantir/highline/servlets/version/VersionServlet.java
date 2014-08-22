package com.palantir.highline.servlets.version;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that will return the version of the current application.
 * <p/>
 * On GET, returns the version string as `text/plain` with a HTTP 200 response.
 */
public class VersionServlet extends HttpServlet {
    private static final long serialVersionUID = -824801600456930313L;

    /**
     * The separater used when building the default version when version detection fails.
     */
    public static final String DEFAULT_MIDFIX = "-dev-";
    public static final int RESPONSE_STATUS = HttpServletResponse.SC_OK;
    public static final String RESPONSE_CONTENT_TYPE = "text/plain";
    public static final String RESPONSE_HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String RESPONSE_CACHE_CONTROL = "must-revalidate,no-cache,no-store";

    private final String version;

    VersionServlet(String version) {
        if (version == null) {
            throw new NullPointerException("Version cannot be null.");
        }

        this.version = version;
    }

    /**
     * Returns a servlet with a fixed version. It will always show the provided version.
     *
     * @param version the version to always return. It cannot be null.
     * @return A VersionServlet.
     */
    public static Servlet withFixedVersion(String version) {
        return new VersionServlet(version);
    }

    /**
     * Returns a servlet with a detected version. If a version cannot be detected, it will use default prefix to build
     * a version, using the current timestamp.
     * <p/>
     * The default version will have the following pattern:
     * <p/>
     * {@code defaultVersionPrefix}-{@link #DEFAULT_MIDFIX}-{@link System#currentTimeMillis()}
     *
     * @param defaultVersionPrefix The prefix used to build a version if none can be detected. It cannot be null.
     * @return A VersionServlet
     */
    public static Servlet detectVersion(String defaultVersionPrefix) {
        if (defaultVersionPrefix == null) {
            throw new NullPointerException("Default Version Prefix cannot be null.");
        }

        String detectedVersion = VersionDetector.detectVersion();

        if (detectedVersion == null) {
            // could not detect a version, build a dynamic using prefix
            detectedVersion = defaultVersionPrefix + DEFAULT_MIDFIX + System.currentTimeMillis();
        }

        return new VersionServlet(detectedVersion);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(RESPONSE_STATUS);
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.addHeader(RESPONSE_HEADER_CACHE_CONTROL, RESPONSE_CACHE_CONTROL);

        try (PrintWriter writer = response.getWriter()) {
            writer.println(version);
        }
    }
}
