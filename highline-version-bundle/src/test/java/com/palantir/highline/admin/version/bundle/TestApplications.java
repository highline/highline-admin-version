package com.palantir.highline.admin.version.bundle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplications {

    private static final String FIXED_VERSION = "1.0.0";
    private static final String DEFAULT_VERSION_PREFIX = "0.0.0";

    public static class FixedVersion extends Application<Configuration> {
        @Override
        public void initialize(Bootstrap<Configuration> bootstrap) {
            bootstrap.addBundle(AdminVersionBundle.withFixedVersion(FIXED_VERSION));
        }

        @Override
        public void run(Configuration configuration, Environment environment) throws Exception {
            environment.jersey().register(new TestResource());
        }
    }

    public static class DetectedVersion extends Application<Configuration> {

        @Override
        public void initialize(Bootstrap<Configuration> bootstrap) {
            bootstrap.addBundle(AdminVersionBundle.detectVersion(DEFAULT_VERSION_PREFIX));
        }

        @Override
        public void run(Configuration configuration, Environment environment) throws Exception {
            environment.jersey().register(new TestResource());
        }
    }

    @Path("/test")
    public static class TestResource {
        @GET
        public String getTest() {
            return "test";
        }
    }
}
