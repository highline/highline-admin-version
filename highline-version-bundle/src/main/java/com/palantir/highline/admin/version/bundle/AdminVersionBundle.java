package com.palantir.highline.admin.version.bundle;

import com.palantir.highline.admin.version.servlet.VersionServlet;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AdminVersionBundle implements Bundle {

    public static final String SERVLET_NAME = "version-servlet";
    public static final String SERVLET_URL = "/version";

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {
        environment.admin()
                .addServlet(SERVLET_NAME, VersionServlet.withFixedVersion(""))
                .addMapping(SERVLET_URL);
    }
}
