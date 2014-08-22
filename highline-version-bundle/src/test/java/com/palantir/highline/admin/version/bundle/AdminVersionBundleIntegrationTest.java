package com.palantir.highline.admin.version.bundle;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Rule;
import org.junit.Test;

import com.palantir.highline.admin.version.servlet.VersionServlet;

import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AdminVersionBundleIntegrationTest {

    private static final String CONFIG_FIXED = "src/test/resources/fixed.yml";
    private static final String CONFIG_DETECT = "src/test/resources/detect.yml";

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
            assertThat(EntityUtils.toString(response.getEntity()), equalTo(expectedVersion));
        }
    }

    public static class FixedTest extends BaseTest {
        @Rule
        public DropwizardAppRule<Configuration> dwRule = new DropwizardAppRule<>(TestApplications.FixedVersion.class, CONFIG_FIXED);

        public FixedTest() {
            super("1.0.0");
        }

        @Override
        public DropwizardAppRule getRule() {
            return dwRule;
        }
    }

    public static class DetectTest extends BaseTest {
        @Rule
        public DropwizardAppRule<Configuration> dwRule = new DropwizardAppRule<>(TestApplications.DetectedVersion.class, CONFIG_DETECT);

        public DetectTest() {
            super("expected");
        }

        @Override
        public DropwizardAppRule getRule() {
            return dwRule;
        }
    }
}
