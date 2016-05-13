package com.github.vincent_fuchs.custom_build_rules;

import com.github.vincent_fuchs.custom_build_rules.files_provider.FilesProvider;
import com.github.vincent_fuchs.custom_build_rules.files_provider.VersionBasedFilesProvider;
import com.github.vincent_fuchs.custom_build_rules.model.Parameters;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import java.io.File;
import java.util.List;

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


    @Override
    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {

        Log log = helper.getLog();

        String version=null;
        String rootDirectory=null;

        try {
            version= (String) helper.evaluate( "${project.version}");
            log.info("project version retrieved from Maven context : "+version);

            rootDirectory=(String) helper.evaluate( "${basedir}");
            log.info("basedir retrieved from Maven context : "+rootDirectory);
        } catch (ExpressionEvaluationException e) {
            log.error("unable to get required infos from Maven context",e);
        }

        Parameters parametersToFindFiles=new Parameters();
        parametersToFindFiles.setVersion(version);
        parametersToFindFiles.setDirectory(rootDirectory+
                com.github.vincent_fuchs.custom_build_rules.util.StringUtils.addLeadingTrailingFileSeparatorIfRequired(directory));
        parametersToFindFiles.setFileExtension(fileExtension);


        if(liquibaseFilesFinder==null){
            liquibaseFilesFinder=new VersionBasedFilesProvider(parametersToFindFiles);
        }

       List<File> filesToCheck=liquibaseFilesFinder.findFiles();

        if(filesToCheck.isEmpty()){
            log.warn("no matching file found in " + parametersToFindFiles.getDirectory()+", with pattern "+liquibaseFilesFinder.getPattern());
        }

       StringBuilder rulesCheckResults=new StringBuilder();

        for(RuleToApply ruleToApply : this.rulesToApply) {

            for (File fileToCheck : filesToCheck) {
                log.info("\n\t\tperforming check on " + fileToCheck.getName());

                String resultForThatFile = ruleToApply.performChecksOn(fileToCheck);

                if (StringUtils.isBlank(resultForThatFile)) {
                    log.info("\t\t\tOK, file is compliant");
                } else {
                    log.warn("\t\t\tSome issues with the file : " + resultForThatFile);
                    rulesCheckResults.append(resultForThatFile).append("\n");
                }
            }
        }

        if(!rulesCheckResults.toString().isEmpty()){
            throw new EnforcerRuleException("some custom files content check have failed - see above logs for details, or below for summary :\n"+rulesCheckResults.toString());
        }
        else{
            log.info("All files are compliant with the custom checks defined");
        }



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
