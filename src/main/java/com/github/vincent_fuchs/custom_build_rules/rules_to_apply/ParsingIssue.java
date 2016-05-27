package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;


import java.io.File;

public class ParsingIssue {

    private String message;

    private File parsedFile;

    public ParsingIssue(String message,File parsedFile) {
        this.message = message;
        this.parsedFile = parsedFile;
    }


    public String getMessage() {
        return message;
    }

    public File getParsedFile() {
        return parsedFile;
    }
}
