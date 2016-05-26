package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    private String patternThatCommentMustFollow;

    @Override
    public String performChecksOn(File fileToCheck) throws IOException {

        String fileContentAsString= readFileAsString(fileToCheck.getAbsolutePath(), StandardCharsets.UTF_8);

        String[] sqlStatements=fileContentAsString.split("/");

        Pattern mandatoryCommentPattern = Pattern.compile(patternThatCommentMustFollow);

        StringBuilder checkResults=new StringBuilder();


            for(int i=0; i < sqlStatements.length ; i++){

                String statementToParse=sqlStatements[i];

                System.out.println("parsing statement "+i+" : "+statementToParse);

                if(statementToParse.contains("CREATE TABLE")){

                    String capturedComment=captureCommentIfAny(statementToParse);
                    if(capturedComment==null){
                        checkResults.append(NO_COMMENT_AT_ALL+" : "+statementToParse);
                    }
                    else{
                        System.out.println("\tcaptured comment : "+capturedComment);
                        if(!mandatoryCommentPattern.matcher(capturedComment).matches()){
                            checkResults.append(COMMENT_NOT_MATCHING_CONFIGURED_PATTERN+" : "+statementToParse);
                            checkResults.append("configured pattern : " + patternThatCommentMustFollow);
                        }
                        else{
                            System.out.println("\tcomment is matching the configured pattern");
                        }
                    }
                }
            }


        return checkResults.toString();

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
