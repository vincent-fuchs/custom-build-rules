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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsingIssue that = (ParsingIssue) o;

        if (!message.equals(that.message)) return false;
        return parsedFile.equals(that.parsedFile);

    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + parsedFile.hashCode();
        return result;
    }
}
