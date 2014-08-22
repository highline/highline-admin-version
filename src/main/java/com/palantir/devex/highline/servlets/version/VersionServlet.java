package com.palantir.devex.highline.servlets.version;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VersionServlet extends HttpServlet {
    public static Servlet withVersion(String version) {
        if (version == null) {
            throw new NullPointerException("Version cannot be null.");
        }
        return new VersionServlet();
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter writer = resp.getWriter()) {
            writer.println("1.0.0");
        }
    }
}
