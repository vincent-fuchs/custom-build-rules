package com.github.vincent_fuchs.custom_build_rules.util;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.*;

import static java.util.Arrays.asList;

public class DirectoryScan {

    private final DirectoryScanner directoryScanner = new DirectoryScanner();

    public List<String> scan(File directory, String pattern) {
        Set<String> set = new LinkedHashSet<>();
        return performScan(directory, pattern);
    }

    private List<String> performScan(File directory, String include) {
        directoryScanner.setBasedir(directory);
        directoryScanner.setIncludes(new String[]{include});
        directoryScanner.setCaseSensitive(false);
        directoryScanner.scan();
        ArrayList<String> result = new ArrayList<>(asList(directoryScanner.getIncludedFiles()));
        Collections.sort(result);
        return result;
    }
}
