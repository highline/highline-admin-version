package com.palantir.highline.admin.version.bundle;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class AdminVersionBundleIntegrationTest {

    @Rule
    public DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<>(
            TestApplications.FixedVersion.class,
            "src/test/resources/fixed.yml");

    @Test
    public void fixedServletWorks() throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://127.0.0.1:" + RULE.getAdminPort() + "/version");

        HttpResponse response = client.execute(get);

        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
