package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class RuleToApply {

    /**
     * Will open the file and parse the content, looking for specific violations (that you would need to implement in the class)
     * @param fileToCheck
     * @return a null or empty String if no violation has been found. A String detailing the found issue(s).
     * It is recommended to be as precise as possible (line number, explanations) so that it's easy to fix for the developer.
     */
    public abstract String performChecksOn(File fileToCheck) throws IOException;


    protected String readFileAsString(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}
