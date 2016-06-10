package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;


import com.github.vincent_fuchs.custom_build_rules.LiquibaseFilesCheck;
import com.github.vincent_fuchs.custom_build_rules.model.ParsedFile;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkipMarkedStatementsRuleToApply extends RuleToApply{

    private String skipPattern;

    public static String SUB_DIRECTORY_FOR_TMP_FILE=File.separator+"target"+File.separator+"tmpFilesWithoutSkippedStatements";

    public SkipMarkedStatementsRuleToApply(String skipPattern) {
        this.skipPattern = skipPattern;
    }

    public SkipMarkedStatementsRuleToApply() {
        super();
    }

    @Override
    public List<ParsingIssue> performChecksOn(ParsedFile file) throws IOException{

        System.out.println("About to perform checks on file "+file.getOriginalFile().getName()+" by "+this.getClass().getName()+"...");

        List<String> notSkippedStatements=new ArrayList<>();

        InputStream fileToCheckAsStream=new FileInputStream(file.getOriginalFile());

        String fileContentAsString= IOUtils.toString(fileToCheckAsStream, "UTF-8");

        String[] statements=fileContentAsString.split("/");

        for(int i=0; i < statements.length ; i++) {

            String statementToParse = statements[i];

            if(!statementToParse.trim().startsWith(skipPattern)){
                notSkippedStatements.add(statementToParse);
            }
        }

        File remainingStatements= writeRemainingStatementsInTmpFile(notSkippedStatements);

        ParsedFile originalAndModifiedFiles=new ParsedFile(file.getOriginalFile(),remainingStatements);

        List<ParsingIssue> parsingIssues=new ArrayList<>();

        if(getNextRuleToApply()!=null) {
            parsingIssues.addAll(getNextRuleToApply().performChecksOn(originalAndModifiedFiles));
        }
        return parsingIssues;

    }

    private File writeRemainingStatementsInTmpFile(List<String> notSkippedStatements) throws IOException {

        File tempDir= new File(LiquibaseFilesCheck.ROOT_DIRECTORY+SUB_DIRECTORY_FOR_TMP_FILE);
        tempDir.mkdirs();

        File tempFileWithoutTheSkippedStatements=File.createTempFile("tmp",null, tempDir);

        String remainingStatements= StringUtils.join(notSkippedStatements,getStatementsSeparator());

        try( PrintWriter out = new PrintWriter( tempFileWithoutTheSkippedStatements )  ){
            out.print( remainingStatements );
            out.print( getStatementsSeparator() );
        }

        return tempFileWithoutTheSkippedStatements;
    }


    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream, File file) throws IOException {

        //should do nothing here.. should never be called

        return new ArrayList<ParsingIssue>();
    }

    public void setSkipPattern(String skipPattern) {
        this.skipPattern = skipPattern;
    }

}
