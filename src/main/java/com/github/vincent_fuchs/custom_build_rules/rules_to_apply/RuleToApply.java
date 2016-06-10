package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public abstract class RuleToApply {

    public static String getStatementsSeparator(){
        return "\n/\n";
    }

    public RuleToApply getNextRuleToApply() {
        return nextRuleToApply;
    }

    public void setNextRuleToApply(RuleToApply nextRuleToApply) {
        this.nextRuleToApply = nextRuleToApply;
    }

    private RuleToApply nextRuleToApply;

    public List<ParsingIssue> performChecksOn(File file) throws IOException{

        System.out.println("About to perform checks on file "+file.getName()+" by "+this.getClass().getName()+"...");

        List<ParsingIssue> parsingIssues=performChecksOn(new FileInputStream(file),file);

        System.out.println("\t -> "+parsingIssues.size()+" issue(s) found");

        if(getNextRuleToApply()!=null) {
            System.out.println("calling next rule..");

            List<ParsingIssue> parsingIssuesForNext=getNextRuleToApply().performChecksOn(file);

            System.out.println("\t-> next rule has found "+parsingIssuesForNext.size()+" issue(s)");

            parsingIssues.addAll(parsingIssuesForNext);
        }

        return parsingIssues;
    }

    /**
     * Will open the file and parse the content, looking for specific violations (that you would need to implement in the class)
     * @param fileToCheckAsStream the content to parse, as an InputStream
     * @param file the file itself : may be required to log precise details if issues are found
     * @return a list of issues, if any. an empty list otherwise.
     * It is recommended to be as precise as possible (line number, explanations) so that it's easy to fix for the developer.
     */
    public abstract List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream,File file) throws IOException;

    public  List<ParsingIssue> performChecksOn(List<String> statements,File file) throws IOException{

        String joinedStatements=StringUtils.join(statements, getStatementsSeparator());

        return performChecksOn(new ByteArrayInputStream(joinedStatements.getBytes(StandardCharsets.UTF_8)),file);
    }



    protected String readFileAsString(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}
