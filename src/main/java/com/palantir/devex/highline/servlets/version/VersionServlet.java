package com.palantir.devex.highline.servlets.version;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VersionServlet extends HttpServlet {
    private static final long serialVersionUID = -824801600456930313L;

    static final String DEFAULT_MIDFIX = "-dev-";
    static final int RESPONSE_STATUS = HttpServletResponse.SC_OK;
    static final String CONTENT_TYPE = "text/plain";
    static final String HEADER_CACHE_CONTROL = "Cache-Control";
    static final String CACHE_CONTROL = "must-revalidate,no-cache,no-store";

    private final String version;

    VersionServlet(String version) {
        if (version == null) {
            throw new NullPointerException("Version cannot be null.");
        }

        this.version = version;
    }

    public static Servlet withFixedVersion(String version) {
        return new VersionServlet(version);
    }

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
        response.setContentType(CONTENT_TYPE);
        response.addHeader(HEADER_CACHE_CONTROL, CACHE_CONTROL);

        try (PrintWriter writer = response.getWriter()) {
            writer.println(version);
        }
    }
}
