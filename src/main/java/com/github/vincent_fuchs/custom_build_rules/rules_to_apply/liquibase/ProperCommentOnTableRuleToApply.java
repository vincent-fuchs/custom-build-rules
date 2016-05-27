package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProperCommentOnTableRuleToApply extends RuleToApply {

    public static final String NO_COMMENT_AT_ALL="This table creation statement should have a comment on it";
    public static final String COMMENT_NOT_MATCHING_CONFIGURED_PATTERN="This table creation statement has a comment that doesn't match the configured pattern";

    private final Pattern capturingComment = Pattern.compile(".*IS[\\n\\r\\s]'(.*)'");

    public void setPatternThatCommentMustFollow(String patternThatCommentMustFollow) {
        this.patternThatCommentMustFollow = patternThatCommentMustFollow;
    }

    private String patternThatCommentMustFollow=".*";

    @Override
    public List<ParsingIssue> performChecksOn(File fileToCheck) throws IOException {

        String fileContentAsString= readFileAsString(fileToCheck.getAbsolutePath(), StandardCharsets.UTF_8);

        String[] sqlStatements=fileContentAsString.split("/");

        Pattern mandatoryCommentPattern = Pattern.compile(patternThatCommentMustFollow);

        List<ParsingIssue> parsingIssues= new ArrayList<>();

        for(int i=0; i < sqlStatements.length ; i++){

                String statementToParse=sqlStatements[i];

                System.out.println("parsing statement "+i+" : "+statementToParse);

                if(statementToParse.contains("CREATE TABLE")){

                    String capturedComment=captureCommentIfAny(statementToParse);
                    if(capturedComment==null){
                        parsingIssues.add(new ParsingIssue(NO_COMMENT_AT_ALL+" : "+statementToParse,fileToCheck));
                    }
                    else{
                        System.out.println("\tcaptured comment : "+capturedComment);
                        if(!mandatoryCommentPattern.matcher(capturedComment).matches()){
                            StringBuilder checkResults=new StringBuilder();
                            checkResults.append(COMMENT_NOT_MATCHING_CONFIGURED_PATTERN+" : "+statementToParse);
                            checkResults.append("configured pattern : " + patternThatCommentMustFollow);

                            parsingIssues.add(new ParsingIssue(checkResults.toString(),fileToCheck));
                        }
                        else{
                            System.out.println("\tcomment is matching the configured pattern");
                        }
                    }
                }
        }

        return parsingIssues;

    }

    private String captureCommentIfAny(String statementToParse) {

        String upperCaseStatement=statementToParse.toUpperCase(Locale.US);

        int positionOfCommentStatement=upperCaseStatement.lastIndexOf("COMMENT ON");

        if(positionOfCommentStatement==-1){
            return null;
        }

        String fullComment=upperCaseStatement.substring(positionOfCommentStatement);
        System.out.println("after COMMENT : "+fullComment);


        Matcher commentMatcher=capturingComment.matcher(fullComment);
        if(!commentMatcher.find()){
            System.out.println("unable to parse the comment : "+fullComment);
            return null;
        }
        else{
            return commentMatcher.group(1);
        }

    }

}
