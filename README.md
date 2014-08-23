highline-admin-version
======================

Add an admin endpoint to a Dropwizard application that reports the version of the service, available
at: `http://ip:adminPort/version`.

Java 1.7+ is required.

Returning a Version
-------------------

The version endpoint is set to `/version`. When sent an HTTP GET request, it will:

- return an `HTTP 200/OK` response
- return `version\n` as plain text
- use the Content Type: `text/plain`


Using It
--------

Using a fixed version:

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {
	    bootstrap.addBundle(AdminVersionBundle.withFixedVersion("1.2.3"));
	}

Using a detected version:

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {
	    bootstrap.addBundle(AdminVersionBundle.detectVersion("1.x"));
	}


What's Available
----------------

There are two separate packages in this project:

- `highline-version-servlet` - just the servlet, with no Dropwizard dependencies.
- `highline-version-bundle` - a Dropwizard bundle that hooks up the servlet.

This division was made so the servlet could be used outside of a Dropwizard application with
minimal changes.


How Detection Works
-------------------

The `Pacakge.getImplementationVersion()` method is used to determine the package.

If no version is detected, one is generated using the provided version prefix.
