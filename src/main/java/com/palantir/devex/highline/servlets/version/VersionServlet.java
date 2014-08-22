package com.palantir.devex.highline.servlets.version;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VersionServlet extends HttpServlet {
    private final String version;

    public VersionServlet(String version) {
        this.version = version;
    }

    public static Servlet withVersion(String version) {
        if (version == null) {
            throw new NullPointerException("Version cannot be null.");
        }
        return new VersionServlet(version);
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
