package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;


import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkipMarkedStatementsRuleToApply extends RuleToApply{

    private String skipPattern;

    private List<String> notSkippedStatements=new ArrayList<>();

    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream, File file) throws IOException {

        String fileContentAsString= IOUtils.toString(fileToCheckAsStream, "UTF-8");

        String[] statements=fileContentAsString.split("/");

        for(int i=0; i < statements.length ; i++) {

            String statementToParse = statements[i];

            if(!statementToParse.startsWith(skipPattern)){
                notSkippedStatements.add(statementToParse);
            }

        }

        return Collections.emptyList();
    }

    public void setSkipPattern(String skipPattern) {
        this.skipPattern = skipPattern;
    }

    public List getNotSkippedStatements() {
        return notSkippedStatements;
    }
}
