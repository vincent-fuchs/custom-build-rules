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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DealOnlyWithExpectedTablesRuleToApply extends RuleToApply {

    private String patternThatTablesMustFollow=".*";

    private final Pattern tableCreation = Pattern.compile(".*CREATE TABLE( IF NOT EXISTS)? \\\"(.*?)\\\".*");
    private final Pattern tableDeletion = Pattern.compile(".*DELETE TABLE( IF NOT EXISTS)? \\\"(.*?)\\\".*");
    private final Pattern insertionPerformed = Pattern.compile(".*INSERT INTO \\\"(.*?)\\\".*");
    private final Pattern alteredTable = Pattern.compile(".*ALTER TABLE (.*?) .*");

    public static final String NOT_ALLOWED_TO_TOUCH_THAT_OBJECT="You shouldn't modify an object that doesn't follow the configured pattern";


    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream, File file) throws IOException {

        List<ParsingIssue> parsingIssues= new ArrayList<>();

        Pattern compliantObject=Pattern.compile(patternThatTablesMustFollow);

        String[] sqlStatements=IOUtils.toString(fileToCheckAsStream, "UTF-8").toUpperCase().split(getStatementsSeparator());

        for(int i=0 ; i<sqlStatements.length;i++){

            Matcher tableCreationMatcher=tableCreation.matcher(sqlStatements[i]);

            if(tableCreationMatcher.matches()){

                String tableCreated=tableCreationMatcher.group(2);

                if(!compliantObject.matcher(tableCreated).find()){
                    parsingIssues.add(new ParsingIssue(NOT_ALLOWED_TO_TOUCH_THAT_OBJECT+" - object : "+tableCreated+". configured pattern : "+patternThatTablesMustFollow,file));
                }
            }


            Matcher insertionPerformedMatcher=insertionPerformed.matcher(sqlStatements[i]);

            if(insertionPerformedMatcher.matches()){

                String tableInWhichWeInsert=insertionPerformedMatcher.group(1);

                if(!tableInWhichWeInsert.matches(patternThatTablesMustFollow)){
                    parsingIssues.add(new ParsingIssue(NOT_ALLOWED_TO_TOUCH_THAT_OBJECT+" - object : "+tableInWhichWeInsert+". configured pattern : "+patternThatTablesMustFollow,file));
                }

            }

            Matcher tableDeletionMatcher=tableDeletion.matcher(sqlStatements[i]);

            if(tableDeletionMatcher.matches()){

                String tableDeleted=tableDeletionMatcher.group(2);

                if(!tableDeleted.matches(patternThatTablesMustFollow)){
                    parsingIssues.add(new ParsingIssue(NOT_ALLOWED_TO_TOUCH_THAT_OBJECT+" - object : "+tableDeleted+". configured pattern : "+patternThatTablesMustFollow,file));
                }

            }

            Matcher tableAlterMatcher=alteredTable.matcher(sqlStatements[i]);

            if(tableAlterMatcher.matches()){

                String tableAltered=tableAlterMatcher.group(1);

                if(!tableAltered.matches(patternThatTablesMustFollow)){
                    parsingIssues.add(new ParsingIssue(NOT_ALLOWED_TO_TOUCH_THAT_OBJECT+" - object : "+tableAltered+". configured pattern : "+patternThatTablesMustFollow,file));
                }

            }
        }

        return parsingIssues ;
    }

    public void setPatternThatTablesMustFollow(String patternThatTablesMustFollow) {
        this.patternThatTablesMustFollow = patternThatTablesMustFollow;
    }
}
