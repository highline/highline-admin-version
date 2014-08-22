package com.palantir.highline.servlets.version;

import javax.annotation.Nullable;

/**
 * Utility Class used to detect the version of the current package.
 */
public class VersionDetector {

    private VersionDetector() {
        // do nothing - utility class
    }

    /**
     * Detects the version of the package, using the {@link Package#getImplementationVersion()} . May return null if
     * could not detect the version.
     *
     * @return Version string
     */
    @Nullable
    public static String detectVersion() {
        // attempt to get the package's implementation version
        Package pkg = VersionDetector.class.getPackage();

        if (pkg != null) {
            return pkg.getImplementationVersion();

        } else {
            // could not get a package, return null
            return null;
        }
    }
}
