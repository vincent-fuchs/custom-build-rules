package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EndingSlashRuleToApplyTest {

    EndingSlashRuleToApply slashRuleToApply = new EndingSlashRuleToApply();

    List<ParsingIssue> parsingIssues;

    @Test
    public void scriptShouldEndWithForwardSlash_WhenLastLineIsAstatement() throws Exception {

        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));

        ParsingIssue parsingIssue=parsingIssues.get(0);

        assertThat(parsingIssue.getMessage()).isEqualTo(EndingSlashRuleToApply.ERROR_MESSAGE);
        assertThat(parsingIssue.getParsedFile().getAbsoluteFile().getName()).endsWith("scriptWithNoEndingSlash.sql");
    }

    @Test
    public void scriptShouldEndWithForwardSlash_validFile() throws Exception {
        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"simpleValidScript.sql"));
        assertThat(parsingIssues).isEmpty();

    }

    @Test
    public void scriptShouldEndWithForwardSlash_validFile_butLastLinesBlank() throws Exception {

        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+ "simpleValidScript_lastLinesHaveBlankCharacters.sql"));
        assertThat(parsingIssues).isEmpty();

    }

    @Test
    public void scriptShouldEndWithForwardSlash_validFile_butLastLinesEmpty() throws Exception {

        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+ "simpleValidScript_lastLinesAreEmpty.sql"));
        assertThat(parsingIssues).isEmpty();

    }

    @Test
    public void scriptShouldEndWithForwardSlash_WhenLastLinesAreBlank() throws Exception {

        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlashAndBlankLines.sql"));

        ParsingIssue parsingIssue=parsingIssues.get(0);

        assertThat(parsingIssue.getMessage()).isEqualTo(EndingSlashRuleToApply.ERROR_MESSAGE);
        assertThat(parsingIssue.getParsedFile().getAbsoluteFile().getName()).endsWith("scriptWithNoEndingSlashAndBlankLines.sql");
    }

    @Test
    public void shouldParseSeveralFilesAndReportErrorsOnlyOnce_validFileFirst() throws Exception {

        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"simpleValidScript.sql"));

        assertThat(parsingIssues).isEmpty();

        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));

        ParsingIssue parsingIssue=parsingIssues.get(0);

        assertThat(parsingIssue.getMessage()).isEqualTo(EndingSlashRuleToApply.ERROR_MESSAGE);
        assertThat(parsingIssue.getParsedFile().getAbsoluteFile().getName()).endsWith("scriptWithNoEndingSlash.sql");

    }

    @Test
    public void shouldParseSeveralFilesAndReportErrorsOnlyOnce_invalidFileFirst() throws Exception {

        parsingIssues = slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));

        ParsingIssue parsingIssue=parsingIssues.get(0);

        assertThat(parsingIssue.getMessage()).isEqualTo(EndingSlashRuleToApply.ERROR_MESSAGE);
        assertThat(parsingIssue.getParsedFile().getAbsoluteFile().getName()).endsWith("scriptWithNoEndingSlash.sql");

        parsingIssues=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"simpleValidScript.sql"));
        assertThat(parsingIssues).isEmpty();
    }


}