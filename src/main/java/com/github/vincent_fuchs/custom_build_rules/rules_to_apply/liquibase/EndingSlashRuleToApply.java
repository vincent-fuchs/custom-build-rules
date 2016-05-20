package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;


import java.io.*;
import java.nio.charset.StandardCharsets;

public class EndingSlashRuleToApply extends RuleToApply {

    public static final String ERROR_MESSAGE="A Liquibase SQL script is expected to end with a \"/\", and this file doesn't";


    @Override
    public String performChecksOn(File fileToCheck) throws IOException {

        String fileContentAsString= readFileAsString(fileToCheck.getAbsolutePath(), StandardCharsets.UTF_8);

        int lastIndexOfSlash=fileContentAsString.lastIndexOf("/");

        String contentAfterLastSlash=fileContentAsString.substring(lastIndexOfSlash+1);

        if(contentAfterLastSlash.isEmpty() || contentAfterLastSlash.matches("[\\n\\r\\s]+")){
            return null;
        }
        else{
            System.out.println("not only blank characters: ");
            System.out.println("***"+contentAfterLastSlash+"***");
            return fileToCheck.getName()+" - "+ERROR_MESSAGE;
        }

    }

}
