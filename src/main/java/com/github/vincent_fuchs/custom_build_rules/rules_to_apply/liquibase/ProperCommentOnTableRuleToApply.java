package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.model.CommentOnTable;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * We may want to enforce some rules on comments, either just to make sure there's one, or more advanced,
 * that they follow a pattern (for post processing later and create synonyms for example)
 */
public class ProperCommentOnTableRuleToApply extends RuleToApply {

    public static final String NO_COMMENT_AT_ALL="This table creation statement should have a comment on it";
    public static final String COMMENT_NOT_MATCHING_CONFIGURED_PATTERN="This table creation statement has a comment that doesn't match the configured pattern";

    private final Pattern capturingComment = Pattern.compile(".* TABLE (.*) IS[\\n\\r\\s]'(.*)'");

    private final Pattern capturingTable = Pattern.compile(".*CREATE TABLE( IF NOT EXISTS)? \\\"(.*?)\\\".*");

    public ProperCommentOnTableRuleToApply(String patternThatCommentMustFollow) {
        this.patternThatCommentMustFollow = patternThatCommentMustFollow;
    }

    public ProperCommentOnTableRuleToApply() {
        super();
    }

    public void setPatternThatCommentMustFollow(String patternThatCommentMustFollow) {
        this.patternThatCommentMustFollow = patternThatCommentMustFollow;
    }

    private String patternThatCommentMustFollow=".*";

    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream, File fileToCheck) throws IOException {

        String fileContentAsString= IOUtils.toString(fileToCheckAsStream, "UTF-8");

        String[] sqlStatements=fileContentAsString.split("/");

        Pattern mandatoryCommentPattern = Pattern.compile(patternThatCommentMustFollow);

        List<ParsingIssue> parsingIssues= new ArrayList<>();

        for(int i=0; i < sqlStatements.length ; i++){

                String statementToParse=sqlStatements[i];

                System.out.println("parsing statement "+i+" : "+statementToParse);

                if(statementToParse.contains("CREATE TABLE")){

                    String tableCreated=captureTable(statementToParse);

                    CommentOnTable capturedComment= captureCommentInCurrentStatement(statementToParse,tableCreated);

                    if(capturedComment==null && i < sqlStatements.length){ //if it is last statement, no need to look in further statements
                        capturedComment= captureCommentInFollowingStatements(Arrays.copyOfRange(sqlStatements, i+1, sqlStatements.length),tableCreated);
                    }


                    if(capturedComment==null){
                        parsingIssues.add(new ParsingIssue(NO_COMMENT_AT_ALL+" : "+statementToParse,fileToCheck));
                    }
                    else{
                        System.out.println("\t"+capturedComment );
                        if(!mandatoryCommentPattern.matcher(capturedComment.getComment()).matches()){
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

    private String captureTable(String statementToParse) {

        Matcher tableMatcher=capturingTable.matcher(statementToParse);
        if(!tableMatcher.find()){
            System.out.println("unable to parse the statement to find the table name : "+statementToParse);
            return null;
        }
        else{
            return tableMatcher.group(2);
        }

    }

    private CommentOnTable captureCommentInFollowingStatements(String[] sqlStatements,String tableForWhichWeSearchForComment) {

        CommentOnTable commentInNextStatement=null;

        for(int i=0; i < sqlStatements.length ; i++) {
            commentInNextStatement=captureCommentInCurrentStatement(sqlStatements[i],tableForWhichWeSearchForComment);

            if(commentInNextStatement!=null){
                return commentInNextStatement;
            }
        }

        return null;
    }

    private CommentOnTable captureCommentInCurrentStatement(String statementToParse, String tableForWhichWeSearchForComment) {

        String upperCaseStatement=statementToParse.trim().toUpperCase(Locale.US);

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

        String foundTable=commentMatcher.group(1);
        String foundComment=commentMatcher.group(2);

        if(!foundTable.equalsIgnoreCase(tableForWhichWeSearchForComment)){

            System.out.println("parse the comment, but table is not matching :");
            System.out.println("\t looking for comments on table "+tableForWhichWeSearchForComment);
            System.out.println("\t found comment for table "+foundTable);
            return null;
        }
        else{
            return new CommentOnTable(foundComment,foundTable);
        }

    }

}
