package com.palantir.highline.admin.version.bundle;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Rule;
import org.junit.Test;

import com.palantir.highline.admin.version.servlet.VersionServlet;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class AdminVersionBundleIntegrationTest {

    private static final String CONFIG_FIXED = "src/test/resources/fixed.yml";
    private static final String CONFIG_DETECT = "src/test/resources/detect.yml";

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

    public static abstract class BaseTest {
        private final String expectedVersion;

        public BaseTest(String expectedVersion) {
            this.expectedVersion = expectedVersion;
        }

        public abstract DropwizardAppRule getRule();

        @Test
        public void fixedServletWorks() throws IOException {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet("http://127.0.0.1:" + getRule().getAdminPort() + "/version");
            HttpResponse response = client.execute(get);

            assertThat(response.getStatusLine().getStatusCode(), equalTo(VersionServlet.RESPONSE_STATUS));
            assertThat(EntityUtils.toString(response.getEntity()), startsWith(expectedVersion));
        }
    }

    public static class FixedTest extends BaseTest {
        @Rule
        public DropwizardAppRule<Configuration> rule = new DropwizardAppRule<>(FixedVersion.class, CONFIG_FIXED);

        public FixedTest() {
            super("1.0.0\n");
        }

        @Override
        public DropwizardAppRule getRule() {
            return rule;
        }
    }

    public static class DetectTest extends BaseTest {
        @Rule
        public DropwizardAppRule<Configuration> rule = new DropwizardAppRule<>(DetectedVersion.class, CONFIG_DETECT);

        public DetectTest() {
            super("expected");
        }

        @Override
        public DropwizardAppRule getRule() {
            return rule;
        }
    }
}
