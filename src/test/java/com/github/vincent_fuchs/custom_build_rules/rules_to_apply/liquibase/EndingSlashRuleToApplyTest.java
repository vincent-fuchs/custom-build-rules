package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class EndingSlashRuleToApplyTest {

    EndingSlashRuleToApply slashRuleToApply = new EndingSlashRuleToApply();

    @Test
    public void scriptShouldEndWithForwardSlash_WhenLastLineIsAstatement() throws Exception {

        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));
        assertThat(checkResult).contains("scriptWithNoEndingSlash.sql - "+ EndingSlashRuleToApply.ERROR_MESSAGE);

    }

    @Test
    public void scriptShouldEndWithForwardSlash_validFile() throws Exception {
        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"simpleValidScript.sql"));
        assertThat(checkResult).isNullOrEmpty();

    }

    @Test
    public void scriptShouldEndWithForwardSlash_validFile_butLastLinesBlank() throws Exception {

        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+ "simpleValidScript_lastLinesHaveBlankCharacters.sql"));
        assertThat(checkResult).isNullOrEmpty();

    }

    @Test
    public void scriptShouldEndWithForwardSlash_validFile_butLastLinesEmpty() throws Exception {

        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+ "simpleValidScript_lastLinesAreEmpty.sql"));
        assertThat(checkResult).isNullOrEmpty();

    }

    @Test
    public void scriptShouldEndWithForwardSlash_WhenLastLinesAreBlank() throws Exception {

        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlashAndBlankLines.sql"));
        assertThat(checkResult).contains("scriptWithNoEndingSlashAndBlankLines.sql - "+ EndingSlashRuleToApply.ERROR_MESSAGE);
    }

    @Test
    public void shouldParseSeveralFilesAndReportErrorsOnlyOnce_validFileFirst() throws Exception {

        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"simpleValidScript.sql"));

        assertThat(checkResult).isNullOrEmpty();

        checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));
        assertThat(checkResult).isEqualTo("scriptWithNoEndingSlash.sql - "+ EndingSlashRuleToApply.ERROR_MESSAGE);
    }

    @Test
    public void shouldParseSeveralFilesAndReportErrorsOnlyOnce_invalidFileFirst() throws Exception {

        String checkResult = slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));
        assertThat(checkResult).isEqualTo("scriptWithNoEndingSlash.sql - "+ EndingSlashRuleToApply.ERROR_MESSAGE);

        checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"simpleValidScript.sql"));
        assertThat(checkResult).isNullOrEmpty();
    }


}