package com.github.vincent_fuchs.custom_build_rules.model;

import java.io.File;

/**
 * Abstraction over regular file, as sometimes we need to create a modified version of the original file,
 * but still need a reference to the original
 */
public class ParsedFile {


    public ParsedFile(File originalFile, File workingFile) {
        this.originalFile = originalFile;
        this.workingFile = workingFile;
    }

    public File getFileToParse(){
        if(workingFile!=null){
            return workingFile;
        }
        else{
            return originalFile;
        }
    }

    public ParsedFile(File originalFile) {
        this.originalFile = originalFile;
    }


    public File getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(File originalFile) {
        this.originalFile = originalFile;
    }

    public File getWorkingFile() {
        return workingFile;
    }

    public void setWorkingFile(File workingFile) {
        this.workingFile = workingFile;
    }

    File originalFile;

    File workingFile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsedFile that = (ParsedFile) o;

        if (!originalFile.equals(that.originalFile)) return false;
        return workingFile != null ? workingFile.equals(that.workingFile) : that.workingFile == null;

    }

    @Override
    public int hashCode() {
        int result = originalFile.hashCode();
        result = 31 * result + (workingFile != null ? workingFile.hashCode() : 0);
        return result;
    }
}
