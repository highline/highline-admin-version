package com.palantir.devex.highline.servlets.version;

public class VersionDetector {

    public static String detectVersion() {
        // attempt to get the package's implementation version
        Package pkg = VersionDetector.class.getPackage();

        if (pkg != null) {
            return pkg.getImplementationVersion();
        } else {
            return null;
        }
    }
}
