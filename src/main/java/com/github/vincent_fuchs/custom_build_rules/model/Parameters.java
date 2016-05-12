package com.github.vincent_fuchs.custom_build_rules.model;

import java.io.File;

public class Parameters {

    private String directory;
    private String version;
    private String fileType;

    public File getDirectory() {
        return new File(directory);
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
