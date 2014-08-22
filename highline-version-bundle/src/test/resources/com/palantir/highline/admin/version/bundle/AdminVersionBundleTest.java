package com.palantir.highline.admin.version.bundle;

import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

import org.junit.Test;

import com.palantir.highline.admin.version.servlet.VersionServlet;

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
        Bundle bundle = new AdminVersionBundle();

        Environment environment = mock(Environment.class);
        AdminEnvironment adminEnvironment = mock(AdminEnvironment.class);
        ServletRegistration.Dynamic dynamic = mock(ServletRegistration.Dynamic.class);

        when(environment.admin()).thenReturn(adminEnvironment);
        when(adminEnvironment.addServlet(anyString(), any(Servlet.class))).thenReturn(dynamic);

        bundle.run(environment);

        verify(adminEnvironment).addServlet(eq(AdminVersionBundle.SERVLET_NAME), any(VersionServlet.class));
        verify(dynamic).addMapping(eq(AdminVersionBundle.SERVLET_URL));
    }

}
