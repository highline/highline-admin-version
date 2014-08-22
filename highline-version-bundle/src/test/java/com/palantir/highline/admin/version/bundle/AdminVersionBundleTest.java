package com.palantir.highline.admin.version.bundle;

import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

import org.junit.Test;

import io.dropwizard.Bundle;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Environment;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminVersionBundleTest {

    @Test
    public void addsServlet() {
        Servlet servlet = mock(Servlet.class);
        Bundle bundle = new AdminVersionBundle(servlet);

        Environment environment = mock(Environment.class);
        AdminEnvironment adminEnvironment = mock(AdminEnvironment.class);
        ServletRegistration.Dynamic dynamic = mock(ServletRegistration.Dynamic.class);

        when(environment.admin()).thenReturn(adminEnvironment);
        when(adminEnvironment.addServlet(anyString(), any(Servlet.class))).thenReturn(dynamic);

        bundle.run(environment);

        verify(adminEnvironment).addServlet(eq(AdminVersionBundle.SERVLET_NAME), eq(servlet));
        verify(dynamic).addMapping(eq(AdminVersionBundle.SERVLET_URL));
    }

    @Test
    public void canBuildWithFixedVersion() {
        AdminVersionBundle.withFixedVersion("1.2.3");
    }

    @Test(expected = NullPointerException.class)
    public void fixedVersionCannotBeNull() {
        AdminVersionBundle.withFixedVersion(null);
    }

    @Test
    public void canBuildWithDetectVersion() {
        AdminVersionBundle.detectVersion("4.5.6");
    }

    @Test(expected = NullPointerException.class)
    public void detectVersionCannotBeNull() {
        AdminVersionBundle.detectVersion(null);
    }
}
