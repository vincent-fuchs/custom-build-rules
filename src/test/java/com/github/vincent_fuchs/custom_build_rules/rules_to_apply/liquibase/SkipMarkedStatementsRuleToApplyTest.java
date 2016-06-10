package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.LiquibaseFilesCheck;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


public class SkipMarkedStatementsRuleToApplyTest {

    SkipMarkedStatementsRuleToApply skipMarkedStatementsRuleToApply=new SkipMarkedStatementsRuleToApply();

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void statementsMarkedAsSkippedShouldNotBeInOutput() throws Exception {

        LiquibaseFilesCheck.ROOT_DIRECTORY=folder.getRoot().getAbsolutePath();

        skipMarkedStatementsRuleToApply.setSkipPattern("-- skipMavenCheck");

        List result=skipMarkedStatementsRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithCommentedStatement.sql"));

        assertThat(result).isEmpty();

        File[] files = new File(LiquibaseFilesCheck.ROOT_DIRECTORY+SkipMarkedStatementsRuleToApply.SUB_DIRECTORY_FOR_TMP_FILE).listFiles();

        assertThat(files).hasSize(1);

        assertThat(files[0]).hasContent("DELETE FROM FEATURES where feature_name='someFeatureName1'\n" +
                RuleToApply.getStatementsSeparator());
    }

}