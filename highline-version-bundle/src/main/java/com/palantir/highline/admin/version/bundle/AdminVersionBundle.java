package com.palantir.highline.admin.version.bundle;

import javax.servlet.Servlet;

import com.google.common.base.Preconditions;
import com.palantir.highline.admin.version.servlet.VersionServlet;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * A Dropwizard {@link io.dropwizard.Bundle} that will add an admin endpoint that will report the version of the
 * service.
 * <p/>
 * The servlet URL is set to {@code /version}.
 * <p/>
 * The constructors mirror the ones available on {@link com.palantir.highline.admin.version.servlet.VersionServlet}
 *
 * @see com.palantir.highline.admin.version.servlet.VersionServlet
 * @see com.palantir.highline.admin.version.servlet.VersionDetector
 */
public class AdminVersionBundle implements Bundle {

    public static final String SERVLET_NAME = "version-servlet";
    public static final String SERVLET_URL = "/version";

    private final Servlet versionServlet;

    AdminVersionBundle(Servlet versionServlet) {
        this.versionServlet = versionServlet;
    }

    /**
     * Creates a bundle with a fixed version.
     *
     * @param version The fixed version to use.
     * @return A bundle.
     */
    public static Bundle withFixedVersion(String version) {
        Preconditions.checkNotNull(version, "Version cannot be null.");
        return new AdminVersionBundle(VersionServlet.withFixedVersion(version));
    }

    /**
     * Creates a bundle that will detect the version, or use a generated one using the provided prefix.
     *
     * @param defaultVersionPrefix The prefix to be used if the version cannot be detected.
     * @return A bundle.
     * @see com.palantir.highline.admin.version.servlet.VersionServlet#detectVersion(String)
     * @see com.palantir.highline.admin.version.servlet.VersionDetector
     */
    public static Bundle detectVersion(String defaultVersionPrefix) {
        Preconditions.checkNotNull(defaultVersionPrefix, "Version cannot be null.");
        return new AdminVersionBundle(VersionServlet.detectVersion(defaultVersionPrefix));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // do nothing
    }

    @Override
    public void run(Environment environment) {
        environment.admin()
                .addServlet(SERVLET_NAME, versionServlet)
                .addMapping(SERVLET_URL);
    }
}
