package com.github.vincent_fuchs.custom_build_rules.files_provider;

import java.util.regex.Pattern;

/**
 * Considering a project version with somePrefix.year.myApp.majorVersion.someSuffix ,
 * it will extract year.myApp.majorVersion as a version. Meaning that full project version can be
 * somePrefix.year.myApp.majorVersion.minorVersion-SNAPSHOT.someSuffix
 * or somePrefix.year.myApp.majorVersion.minorVersion-RCXX.someSuffix
 * --> major version extracted will be year.myApp.majorVersion
 */
public class MajorVersionExtractor implements VersionExtractor{

    private static final String DOT = ".";

    @Override
    public String extractVersion(String projectVersion) {

        String[] words=projectVersion.split(Pattern.quote("."));
        String year=words[0];
        String moduleName=words[1];
        String versionNumber=words[2];
        String exactVersion=year+DOT+moduleName+DOT+versionNumber;
        return exactVersion;

    }
}
