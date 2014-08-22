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
    private final String version;

    public VersionServlet(String version) {
        this.version = version;
    }

    public static Servlet withFixedVersion(String version) {
        if (version == null) {
            throw new NullPointerException("Version cannot be null.");
        }
        return new VersionServlet(version);
    }

    public static Servlet detectVersion(String defaultVersionPrefix) {
        if (defaultVersionPrefix == null) {
            throw new NullPointerException("Default Version Prefix cannot be null.");
        }

        String detectedVersion = VersionDetector.detectVersion();

        if (detectedVersion == null) {
            // could not detect a version, build a dynamic using prefix
            detectedVersion = defaultVersionPrefix + "-dev-" + System.currentTimeMillis();
        }

        return new VersionServlet(detectedVersion);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.addHeader("Cache-Control", "must-revalidate,no-cache,no-store");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(version);
        }
    }
}
