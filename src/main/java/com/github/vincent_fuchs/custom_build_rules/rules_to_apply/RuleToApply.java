package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class RuleToApply {


    public List<ParsingIssue> performChecksOn(File file) throws IOException{

        return performChecksOn(new FileInputStream(file),file);

    }

    /**
     * Will open the file and parse the content, looking for specific violations (that you would need to implement in the class)
     * @param fileToCheckAsStream the content to parse, as an InputStream
     * @param file the file itself : may be required to log precise details if issues are found
     * @return a list of issues, if any. an empty list otherwise.
     * It is recommended to be as precise as possible (line number, explanations) so that it's easy to fix for the developer.
     */
    public abstract List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream,File file) throws IOException;


    protected String readFileAsString(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}
