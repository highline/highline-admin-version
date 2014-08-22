package com.palantir.highline.admin.version.bundle;

import com.google.common.base.Preconditions;
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
                .addServlet(SERVLET_NAME, VersionServlet.withFixedVersion("thisShouldntWork"))
                .addMapping(SERVLET_URL);
    }

    public static Bundle withFixedVersion(String version) {
        Preconditions.checkNotNull(version, "Version cannot be null.");
        return new AdminVersionBundle();
    }

    public static Bundle detectVersion(String defaultVersionPrefix) {
        Preconditions.checkNotNull(defaultVersionPrefix, "Version cannot be null.");
        return new AdminVersionBundle();
    }
}
