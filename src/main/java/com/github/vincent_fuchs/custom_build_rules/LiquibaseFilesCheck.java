package com.github.vincent_fuchs.custom_build_rules;

import com.github.vincent_fuchs.custom_build_rules.files_provider.FilesProvider;
import com.github.vincent_fuchs.custom_build_rules.files_provider.VersionBasedFilesProvider;
import com.github.vincent_fuchs.custom_build_rules.model.Parameters;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LiquibaseFilesCheck implements EnforcerRule {

    public void setRulesToApply(List<RuleToApply> rulesToApply) {
        this.rulesToApply = rulesToApply;
    }

    public FilesProvider getFilesProvider() {
        return liquibaseFilesFinder;
    }

    public void setFilesProvider(FilesProvider filesProvider) {
        this.liquibaseFilesFinder = filesProvider;
    }

    List<RuleToApply> rulesToApply;

    FilesProvider liquibaseFilesFinder;

    String directory;

    String fileExtension;

    public static String ROOT_DIRECTORY =null;


    @Override
    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {

        Log log = helper.getLog();

        String version=null;


        try {
            version= (String) helper.evaluate( "${project.version}");
            log.info("project version retrieved from Maven context : "+version);

            ROOT_DIRECTORY =(String) helper.evaluate( "${basedir}");
            log.info("basedir retrieved from Maven context : "+ ROOT_DIRECTORY);
        } catch (ExpressionEvaluationException e) {
            log.error("unable to get required infos from Maven context",e);
        }

        Parameters parametersToFindFiles=new Parameters();
        parametersToFindFiles.setVersion(version);
        parametersToFindFiles.setDirectory(ROOT_DIRECTORY +
                com.github.vincent_fuchs.custom_build_rules.util.StringUtils.addLeadingTrailingFileSeparatorIfRequired(directory));
        parametersToFindFiles.setFileExtension(fileExtension);


        if(liquibaseFilesFinder==null){
            liquibaseFilesFinder=new VersionBasedFilesProvider(parametersToFindFiles);
        }

        List<File> filesToCheck=liquibaseFilesFinder.findFiles();

        if(filesToCheck.isEmpty()){
            log.warn("no matching file found in " + parametersToFindFiles.getDirectory()+", with pattern "+liquibaseFilesFinder.getPattern());
        }

        List<ParsingIssue> parsingIssues = new ArrayList<>();

        if(rulesToApply.isEmpty()){
            throw new IllegalStateException("At least one RuleToApply must be configured");
        }

        RuleToApply chainedRulesToApply=buildChainOfResponsability(rulesToApply);
        
        
        for (File fileToCheck : filesToCheck) {

            log.info("\n\t\tperforming check on " + fileToCheck.getName());

            try {
                parsingIssues.addAll(chainedRulesToApply.performChecksOn(fileToCheck));
            } catch (IOException e) {
                parsingIssues.add(new ParsingIssue("issue while parsing",fileToCheck));
                log.error("couldn't parse the file at all",e);
            }

            if (parsingIssues.isEmpty()) {
                log.info("\t\t\tOK, file is compliant");
            }
            else {
                log.warn("\t\t\tSome issues with the file : " + parsingIssues);
            }
        }

        if(!parsingIssues.isEmpty()){
            throw new EnforcerRuleException("some custom files content check have failed - see above logs for details, or below for summary :\n"+logNicely(parsingIssues));
        }
        else{
            log.info("All files are compliant with the custom checks defined");
        }

    }

    private RuleToApply buildChainOfResponsability(List<RuleToApply> rulesToApply) {

        RuleToApply initialRuleToApply=rulesToApply.get(0);

        for(int i=1 ; i<rulesToApply.size() ; i++){
            rulesToApply.get(i-1).setNextRuleToApply(rulesToApply.get(i));
        }

        return initialRuleToApply;
    }

    private String logNicely(List<ParsingIssue> parsingIssues){

        Map<File,List<String>> issuesPerFile=new HashMap<>();

        for(ParsingIssue issue : parsingIssues){

            if(!issuesPerFile.containsKey(issue.getParsedFile())){
                issuesPerFile.put(issue.getParsedFile(),new ArrayList<String>());
            }

            issuesPerFile.get(issue.getParsedFile()).add(issue.getMessage());
        }

        StringBuilder sb=new StringBuilder();

        for(Map.Entry<File,List<String>> entry : issuesPerFile.entrySet()){
            sb.append("issues for file "+entry.getKey().getName()+" : \n");
            for(String issue : entry.getValue()){
                sb.append("\t - "+issue+"\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean isResultValid(EnforcerRule enforcerRule) {
        return false;
    }

    @Override
    public String getCacheId() {
        return null;
    }
}