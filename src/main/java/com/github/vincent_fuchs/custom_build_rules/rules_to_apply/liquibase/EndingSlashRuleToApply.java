package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.files_provider.FilesProvider;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;

import java.io.*;

public class EndingSlashRuleToApply implements RuleToApply {
    private File file;
    private String message="";

    public static final String ERROR_MESSAGE="A Liquibase SQL script is expected to end with a \"/\", and this file doesn't";


    @Override
    public String performChecksOn(File fileToCheck) throws IOException {
        this.file=fileToCheck;
        applyRuleInFile();
        return message;
    }

    private void applyRuleInFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String firstLine="";
        String secondLine = "";
        while ((firstLine = bufferedReader.readLine()) != null) {
                if(!firstLine.isEmpty() && firstLine.charAt(0) != '/'){
                  secondLine = bufferedReader.readLine();
                    if(secondLine==null || secondLine.charAt(0)!='/'){
                        message=file.getName()+" - "+ERROR_MESSAGE;
                    }
                }
        }
        bufferedReader.close();
    }
}
