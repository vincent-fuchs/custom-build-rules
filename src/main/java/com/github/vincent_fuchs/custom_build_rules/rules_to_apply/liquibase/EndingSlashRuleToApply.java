package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.apache.commons.io.IOUtils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class EndingSlashRuleToApply extends RuleToApply {

    public static final String ERROR_MESSAGE="This file is expected to end with a \"/\", but it doesn't";

    private static Pattern allBlankCharacters = Pattern.compile("[\\n\\r\\s]+");

    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream,File fileToCheck) throws IOException {

        String fileContentAsString= IOUtils.toString(fileToCheckAsStream, "UTF-8");

        int lastIndexOfSlash=fileContentAsString.lastIndexOf("/");

        String contentAfterLastSlash=fileContentAsString.substring(lastIndexOfSlash+1);

        if(contentAfterLastSlash.isEmpty() || allBlankCharacters.matcher(contentAfterLastSlash).matches()){
            return Collections.emptyList();
        }
        else{
            System.out.println("not only blank characters: ");
            System.out.println("***"+contentAfterLastSlash+"***");

            return Arrays.asList(new ParsingIssue(ERROR_MESSAGE,fileToCheck));
        }

    }

}
