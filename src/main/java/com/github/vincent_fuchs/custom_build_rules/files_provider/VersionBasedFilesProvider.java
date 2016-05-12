package com.github.vincent_fuchs.custom_build_rules.files_provider;

import com.github.vincent_fuchs.custom_build_rules.model.Parameters;
import com.github.vincent_fuchs.custom_build_rules.util.DirectoryScan;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Based on the project's version, this FileProvider will find all the files in the configured path, that start with the prefix+projectVersion pattern.
 */
public class VersionBasedFilesProvider implements FilesProvider{

    private File directory;

    public VersionBasedFilesProvider() {
    }

    public String getVersion() {
        return version;
    }

    String version;
    String prefix = "**/*";
    private String fileType;

    public String getPattern() {
        return pattern;
    }

    private String pattern;
    private String dot = ".";
    private String star = "*";

    private final DirectoryScanner directoryScanner = new DirectoryScanner();

    public VersionBasedFilesProvider(Parameters parameters) {
        this.version=parameters.getVersion();
        this.directory = parameters.getDirectory();
        this.fileType = parameters.getFileExtension();
        this.pattern = prefix+version+star+dot+fileType;
    }

    @Override
    public List<File> findFiles() {
        DirectoryScan directoryScan = new DirectoryScan();
        List<File> files = new ArrayList<>();
        List<String> allFiles = directoryScan.scan(directory, pattern);
        for (String fileName : allFiles) {
            files.add(new File(directory+ File.separator+fileName));
        }

        return files;
    }



}
